import os
from threading import Thread
import time
import requests
import pandas as pd
from bs4 import BeautifulSoup
from datetime import datetime, timedelta
import re

from exceptions import NoOpException


class FilterParallelizer:

    time_start = None
    time_end = None
    all_companies_data = dict()

    def __init__(self):
        self.companies = self.__fetch_companies()

    # 1st Filter
    def __fetch_companies(self):
        """b
        :return: A List of all the companies available on the MSE website
        """

        base = 'https://www.mse.mk/en/issuers/shares-listing'

        response = requests.get(base)

        soup = BeautifulSoup(response.text, 'html.parser')

        links = map(lambda x: x.get('href'), soup.find_all('a', attrs={'href': re.compile("^/en/issuer/")}))


        valid_company_names = []

        for link in links:
            resp = requests.get('https://www.mse.mk' + str(link))
            soup = BeautifulSoup(resp.text, 'html.parser')
            valid_company_names += list(map(lambda x: None if any(char.isdigit() for char in x.text) else x.text, soup.select('#symbols > li > a')))

        special_reporting = 'https://www.mse.mk/prefs/issuers/JSC-with-special-reporting-obligations'
        free_market = 'https://www.mse.mk/prefs/issuers/free-market'

        resp = requests.get(special_reporting)

        soup = BeautifulSoup(resp.text, 'html.parser')

        print()

        valid_company_names += list(map(lambda x: x.text, soup.select('#otherlisting-table > tbody > tr > td:nth-child(1) > a')))

        resp = requests.get(free_market)

        soup = BeautifulSoup(resp.text, 'html.parser')

        valid_company_names += list(map(lambda x: x.text, soup.select('#otherlisting-table > tbody > tr > td:nth-child(1) > a')))

        valid_company_names = list(set(valid_company_names))

        print(len(valid_company_names))

        final = [company for company in valid_company_names if company is not None]

        print(len(final))

        return final

    def __create_table(self, company_data: list):

        df = pd.DataFrame(company_data)
        df.columns = ['Date', 'LastTradePrice', 'Max', 'Min', 'AvgPrice', '%chg', "Volume", 'TurnoverBestMKD', 'TotalTurnoverMKD']

        return df

    # Part of the 3rd filter
    def parse_cells(self, row):

        # Number formatter
        translation_table = str.maketrans({',': '.',
                                           '.': ','})

        cells = row.find_all('td')
        date = cells[0].text
        last_trade_price = cells[1].text.translate(translation_table)
        max = cells[2].text.translate(translation_table)
        min = cells[3].text.translate(translation_table)
        avg_price = cells[4].text.translate(translation_table)
        chg = cells[5].text.translate(translation_table)
        volume = cells[6].text
        turnover_in_best = cells[7].text.translate(translation_table)
        total_turnover = cells[8].text.translate(translation_table)

        return [date, last_trade_price, max, min, avg_price, chg, volume, turnover_in_best, total_turnover]

    def __get_site_data(self, soup):
        # Accepts a BeautifulSoup object
        table = soup.find_all('tbody')
        if len(table) == 0:
            return None
        table = table[0]
        table_rows = table.find_all('tr')
        ret_table = []

        for row in table_rows:
            ret_table.append(self.parse_cells(row))

        return ret_table

    def __get_x_days_ago_of(self, date, days):
        date_from = datetime.strptime(date, '%m/%d/%Y') - timedelta(days=days)
        return date_from.strftime('%m/%d/%Y').__str__()

    # 3rd Filter
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

        for i in range(1, years + 2):

            if i == (years + 1):
                date_from = datetime.strptime(date_to, '%m/%d/%Y') - timedelta(days=daysleft)
                date_from = date_from.strftime('%m/%d/%Y').__str__()

            url = base_url + company + "?" + "FromDate=" + date_from + '&ToDate=' + date_to
            response = requests.post(url, timeout=(25, 60))

            while response.status_code != 200:
                response = requests.post(url, timeout=(25, 60))

            soup = BeautifulSoup(response.text, 'html.parser')

            data_append = self.__get_site_data(soup)

            if data_append is not None:
                company_info += data_append

            date_to = date_from
            date_from = self.__get_x_days_ago_of(date_to, 365)

        table = self.__create_table(company_info)
        table.to_csv(f'./data/{company}.csv')
        self.all_companies_data[company] = table

    # 2nd Filter
    def fill_data(self):

        dataframes = dict()
        threadpool = []
        today = str(datetime.today().strftime('%m/%d/%Y'))

        self.time_start = time.time()

        for cmp in self.companies:

            if f'{cmp}.csv' not in os.listdir('./data'):
                search_from = self.__get_x_days_ago_of(today, 365 * 10)
            else:
                curr_df = pd.read_csv(f'./data/{cmp}.csv')
                search_from = str((datetime.strptime(curr_df.Date[0], '%m/%d/%Y') + timedelta(days=1)).strftime('%m/%d/%Y'))
                yesterday = (datetime.today() - timedelta(days=1)).strftime('%m/%d/%Y')

                if today == search_from or yesterday == search_from:
                    continue

                dataframes[cmp] = curr_df.drop(columns=['Unnamed: 0'])

            # Pass to 3rd Filter
            thread = Thread(target=self.__get_data_from_to, args=(cmp, search_from, today))
            thread.start()
            threadpool.append(thread)

        for thread in threadpool:
            thread.join()

        for cmp in self.companies:
            if cmp in self.all_companies_data:
                df = pd.concat(
                    [self.all_companies_data[cmp], dataframes[cmp] if cmp in dataframes.keys() else pd.DataFrame()],
                    axis=0)
                df=df.reset_index().drop(columns=['index'])
                df.to_csv(f'./data/{cmp}.csv')

        self.time_end = time.time()

    def get_time_last_scrape(self):

        if self.time_end is None:
            raise NoOpException('Please fill the database first by running the fill_data() method')

        time_s = (self.time_end - self.time_start)

        return f'{int(time_s / 60)}m {int(time_s % 60)}s'
