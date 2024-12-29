import os
import pandas as pd
import numpy as np
np.NaN = np.nan
from pandas import DataFrame
import pandas_ta as ta


# oscillators
def RSI(data):
    return ta.rsi(data['Price'], timeperiod=14)


def STOCH(data):
    stoch_df = ta.stoch(high=data['Max'], low=data['Min'], close=data['Price'], timeperiod=14)
    return stoch_df['STOCHk_14_3_3'], stoch_df['STOCHd_14_3_3']


def ROC():
    return ta.roc(df['Price'], timeperiod=14)


def WILLIAMS_R():
    return ta.willr(high=df['Max'], low=df['Min'], close=df['Price'])


def MOMENTUM():
    return ta.mom(df['Price'])


# moving averages
def simple_moving_avg(data):
    window_size = [1, 7, 30]
    df = DataFrame()
    for size in window_size:
        col_name = str(size) + '_moving_avg'
        df[col_name] = data.rolling(window=size).mean()
    return df


def exp_moving_avg(data):
    com = [1, 7, 30]
    df = DataFrame()
    for size in com:
        col_name = str(size) + '_exp_moving_avg'
        df[col_name] = data.ewm(com=size).mean()
    return df


def weighted_moving_avg(data):
    window_size = [1, 7, 30]
    df = DataFrame()
    for size in window_size:
        col_name = str(size) + '_wma'
        df[col_name] = ta.wma(data, timeperiod=size)
    return df


def double_exp_moving_avg(data):
    window_size = [1, 7, 30]
    df = DataFrame()
    for size in window_size:
        col_name = str(size) + '_dema'
        df[col_name] = ta.dema(data, timeperiod=size)
    return df


def triple_exp_moving_avg(data):
    window_size = [1, 7, 30]
    df = DataFrame()
    for size in window_size:
        col_name = str(size) + '_tema'
        df[col_name] = ta.tema(data, timeperiod=size)
    return df


def parse_prices(prices):
    prices = prices.astype(str)
    return [float(price.split(',')[0].replace('.', '')) for price in prices if price is not None]


if __name__ == '__main__':

    root = 'src/main/resources'
    files = os.listdir(f'{root}/db')

    for file in files:
        company_name = file.split(".")[0]
        datapath = f'{root}/db/{file}'
        series = pd.read_csv(datapath)

        if len(series[series['LastTradePrice'].isna()]) > 0:
            continue

        df = DataFrame()

        # parsing columns needed for calculations
        df['Date'] = pd.to_datetime(series['Date'])
        df = df.sort_values(by='Date', ascending=True)
        df['Price'] = pd.to_numeric(parse_prices(series['LastTradePrice']))

        df['Min'] = pd.to_numeric(parse_prices(series['Min']))
        df['Max'] = pd.to_numeric(parse_prices(series['Max']))

        drop_min = df['Min'].isna().sum() / len(df) < 0.1
        drop_max = df['Max'].isna().sum() / len(df) < 0.1

        if drop_min and drop_max:
            df = df[df['Min'].notna() & df['Max'].notna()]

        inc_minmax_oscillators = len(df[df['Min'].isna()]) == 0 and len(df[df['Max'].isna()]) == 0

        # adding columns that are technical indicators
        df['RSI'] = pd.to_numeric(RSI(df))
        if inc_minmax_oscillators:
            df['K_STOCH'], df['D_STOCH'] = STOCH(df)
        else:
            df['K_STOCH'] = np.array([np.nan for _ in range(len(df))])
            df['D_STOCH'] = np.array([np.nan for _ in range(len(df))])
        df['ROC'] = ROC()
        df['MOMENTUM'] = MOMENTUM()
        if inc_minmax_oscillators:
            df['WILLIAMS'] = WILLIAMS_R()
        else:
            df['WILLIAMS'] = np.array([np.nan for _ in range(len(df))])

        # adding columns that are MA's
        SMA = simple_moving_avg(df['Price'])
        df = pd.concat([df, SMA], axis=1)

        EMA = exp_moving_avg(df['Price'])
        df = pd.concat([df, EMA], axis=1)

        WMA = weighted_moving_avg(df['Price'])
        df = pd.concat([df, WMA], axis=1)

        DEMA = double_exp_moving_avg(df['Price'])
        df = pd.concat([df, DEMA], axis=1)

        TEMA = triple_exp_moving_avg(df['Price'])
        df = pd.concat([df, TEMA], axis=1)


        # removes all the possible nans from moving averages
        df = df.iloc[100:, :]

        print(f"COMPANY: {company_name} analysis successful.")

        df.to_csv(f"{root}/ta/{company_name}_ta.csv", index=False)

    exit(0)