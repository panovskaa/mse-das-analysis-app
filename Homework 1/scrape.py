from parallel_filters import FilterParallelizer

if __name__ == '__main__':

    para = FilterParallelizer()
    para.fill_data()

    print(len(para.all_companies_data))
    print(para.get_time_last_scrape())
