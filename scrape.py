import requests
from bs4 import BeautifulSoup
from parallel_filters import FilterParallelizer

from exceptions import *

if __name__ == '__main__':

    SAMPLE_URL = 'https://www.mse.mk/en/stats/symbolhistory/ADIN?FromDate=11/1/2023&ToDate=11/3/2023'

    response = requests.post(SAMPLE_URL)

    soup = BeautifulSoup(response.text, 'html.parser')

    company_names = soup.select('.form-control option')

    # GETS COMPANY NAMES FROM A SAMPLE URL
    valid_company_names = []

    for name in company_names:
        if not (any(char.isdigit() for char in name.text)):
            valid_company_names.append(name.text)

    print(len(valid_company_names))

    para = FilterParallelizer(valid_company_names)
    para.fill_db()

    print(len(para.all_companies_data))
    print(para.get_time_last_scrape())
