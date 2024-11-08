import os
from threading import Thread
import time
import requests
import pandas as pd
from bs4 import BeautifulSoup
from datetime import datetime, timedelta

from exceptions import NoOpException, AlreadyUpdatedException


class FilterParallelizer:
    time_start = None
    time_end = None
    all_companies_data = dict()

    def __init__(self, companies):
        self.companies = companies

    def __create_table(self, company_data: list):
        data = list(map(lambda x: x.values(), company_data))
        df = pd.DataFrame(data=data)
        df.columns = company_data[0].keys()
        return df

    def parse_cells(self, row):
        cells = row.find_all('td')
        date = cells[0].text
        last_trade_price = cells[1].text
        max = cells[2].text
        min = cells[3].text
        avg_price = cells[4].text
        chg = cells[5].text
        volume = cells[6].text
        turnover_in_best = cells[7].text
        total_turnover = cells[8].text

        item = {
            'Date': date,
            'Last trade price': last_trade_price,
            'Max': max,
            'Min': min,
            'AvgPrice': avg_price,
            '%chg.': chg,
            'Volume': volume,
            'TurnoverBEST_MKD': turnover_in_best,
            'TotalTurnoverMKD': total_turnover
        }

        return item

    def __get_site_data(self, soup, company):
        # Accepts a BeautifulSoup object
        table = soup.find_all('tbody')
        if len(table) == 0:
            return None
        table = table[0]
        table_rows = table.find_all('tr')
        for row in table_rows:
            item = self.parse_cells(row)
            company.append(item)

        return company

    def __get_x_days_ago_of(self, date, days):
        date_from = datetime.strptime(date, '%m/%d/%Y') - timedelta(days=days)
        return date_from.strftime('%m/%d/%Y').__str__()

    def __get_data_from_to(self, company, date_from, date_to):
        base_url = 'https://www.mse.mk/en/stats/symbolhistory/'

        date_from_obj = datetime.strptime(date_from, '%m/%d/%Y')
        date_to_obj = datetime.strptime(date_to, '%m/%d/%Y')

        days = (date_to_obj - date_from_obj).days
        years = days // 365
        daysleft = days % 365

        date_from = self.__get_x_days_ago_of(date_to, 365)

        if years == 0:
            date_from = self.__get_x_days_ago_of(date_to, daysleft)

        company_info = []

        print(company)

        for i in range(1, years + 2):

            if i == (years + 1):
                date_from = datetime.strptime(date_to, '%m/%d/%Y') - timedelta(days=daysleft)
                date_from = date_from.strftime('%m/%d/%Y').__str__()

            url = base_url + company + "?" + "FromDate=" + date_from + '&ToDate=' + date_to
            response = requests.post(url, timeout=(25, 60))

            if response.status_code != 200:
                raise ConnectionError('Failed to get a significant response from the page')

            soup = BeautifulSoup(response.text, 'html.parser')

            yearly_thread = Thread(target=self.__get_site_data, args=(soup, company_info))
            yearly_thread.start()

            date_to = date_from
            date_from = self.__get_x_days_ago_of(date_to, 365)

            yearly_thread.join()

        table = self.__create_table(company_info)
        table.to_csv(f'./data/{company}.csv')
        self.all_companies_data[company] = table

    def fill_missing_data(self):

        if len(os.listdir('./data')) != 1:
            self.fill_db()
            return

        dataframes = dict()
        threadpool = []

        self.time_start = time.time()

        for cmp in self.companies:
            curr_df = pd.read_csv(f'./data/{cmp}.csv')
            last_seen = str(datetime.strptime(curr_df.Date[0], '%m/%d/%Y').strftime('%m/%d/%Y'))
            today = str(datetime.today().strftime('%m/%d/%Y'))
            yesterday = (datetime.today() - timedelta(days=1)).strftime('%m/%d/%Y')

            if today == last_seen or yesterday == last_seen:
                raise AlreadyUpdatedException("The database is already updated with the latest available data.")

            thread = Thread(target=self.__get_data_from_to, args=(cmp, last_seen, today))
            thread.start()
            threadpool.append(thread)
            dataframes[cmp] = curr_df.drop(columns=['Unnamed: 0'])

        for thread in threadpool:
            thread.join()

        for cmp in self.companies:
            if cmp in self.all_companies_data.keys():
                df = pd.concat([self.all_companies_data[cmp], dataframes[cmp]], axis=0)
                df.to_csv(f'./data/{cmp}.csv')

        self.time_end = time.time()

    def fill_db(self):
        today = str(datetime.today().strftime('%m/%d/%Y'))
        ten_years_ago = self.__get_x_days_ago_of(today, 365 * 10)

        self.time_start = time.time()
        threadpool = []

        for cmp in self.companies:
            thread = Thread(
                target=self.__get_data_from_to,
                args=(cmp, ten_years_ago, today)
            )
            thread.start()
            threadpool.append(thread)

        for thread in threadpool:
            thread.join()

        self.time_end = time.time()

        print(self.get_time_last_scrape())

    def format(self, df):
        """
        :param df: DataFrame object in which the columns which contain real numbers are formatted
        :return: A properly (desired) number-formatted pd.DataFrame
        """

    def get_time_last_scrape(self):
        if self.time_end is None:
            raise NoOpException("No operation has yet been performed, please fill the database first using fill_db,"
                                "or, call a fetch missing operation by fill_missing_data")

        time_s = (self.time_end - self.time_start)

        return f'{int(time_s / 60)}m {int(time_s % 60)}s'
