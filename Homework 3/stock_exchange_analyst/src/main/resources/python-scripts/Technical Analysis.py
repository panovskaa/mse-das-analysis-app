import pandas as pd
from pandas import DataFrame
import pandas_ta as ta


# technical indicators
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
    window_size = [10, 20, 50, 100, 200]
    df = DataFrame()
    for size in window_size:
        col_name = str(size) + '_moving_avg'
        df[col_name] = data.rolling(window=size).mean()
    return df


def exp_moving_avg(data):
    com = [10, 20, 50, 100, 200]
    df = DataFrame()
    for size in com:
        col_name = str(size) + '_exp_moving_avg'
        df[col_name] = data.ewm(com=size).mean()
    return df


def weighted_moving_avg(data):
    window_size = [10, 20, 50, 100, 200]
    df = DataFrame()
    for size in window_size:
        col_name = str(size) + '_wma'
        df[col_name] = ta.wma(data, timeperiod=size)
    return df


def double_exp_moving_avg(data):
    window_size = [10, 20, 50, 100, 200]
    df = DataFrame()
    for size in window_size:
        col_name = str(size) + '_dema'
        df[col_name] = ta.dema(data, timeperiod=size)
    return df


def triple_exp_moving_avg(data):
    window_size = [10, 20, 50, 100, 200]
    df = DataFrame()
    for size in window_size:
        col_name = str(size) + '_tema'
        df[col_name] = ta.tema(data, timeperiod=size)
    return df


def parse_prices(prices):
    return [float(price.split(',')[0].replace('.', '')) for price in prices if price is not None]


if __name__ == '__main__':
    company_name = "ALK"
    datapath = company_name + ".csv"
    series = pd.read_csv(datapath)

    series = series.dropna()
    df = DataFrame()

    # parsing columns needed for calculations
    df['Date'] = pd.to_datetime(series['Date'])
    df = df.sort_values(by='Date', ascending=True)
    df['Price'] = pd.to_numeric(parse_prices(series['LastTradePrice']))
    df['Min'] = pd.to_numeric(parse_prices(series['Min']))
    df['Max'] = pd.to_numeric(parse_prices(series['Max']))

    # adding columns that are technical indicators
    df['RSI'] = pd.to_numeric(RSI(df))
    df['K_STOCH'], df['D_STOCH'] = STOCH(df)
    df['ROC'] = ROC(df)
    df['MOMENTUM'] = MOMENTUM(df)
    df['WILLIAMS'] = WILLIAMS_R(df)

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

    # saved without date as index
    df = df.dropna()
    df.to_csv(company_name + "_technical_analysis.csv", index=False)
