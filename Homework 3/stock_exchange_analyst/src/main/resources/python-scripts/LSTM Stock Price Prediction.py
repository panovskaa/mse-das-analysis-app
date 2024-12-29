import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sns
from pandas.core.interchange.dataframe_protocol import DataFrame
from sklearn.model_selection import train_test_split
from sklearn.metrics import r2_score
from sklearn.preprocessing import MinMaxScaler
from pandas import DataFrame
from keras.api.models import Sequential
from keras.api.layers import Input, LSTM, Dense


def read_csv(company_name):
    path = str(company_name) + ".csv"
    dataset = pd.read_csv(path)
    dataset = dataset.dropna()

    new_dataset = DataFrame()

    new_dataset['Date'] = pd.to_datetime(dataset['Date'])
    prices = [float(price.split(',')[0].replace('.', '')) for price in dataset['LastTradePrice'] if price is not None]

    new_dataset['Price'] = pd.to_numeric(prices, errors='coerce')
    new_dataset = new_dataset.set_index('Date')

    return new_dataset


if __name__ == '__main__':
    df = read_csv("ALK")
    df = df.head(365)
    lag = 14
    periods = range(lag, 0, -1)

    df = pd.concat([df, df.shift(periods=periods)], axis=1)
    df = df.dropna()

    X, y = df.drop(['Price'], axis=1), df['Price']

    train_X, test_X, train_y, test_y = train_test_split(X, y, test_size=0.3, shuffle=False)

    scaler = MinMaxScaler()
    train_X = scaler.fit_transform(train_X)
    test_X = scaler.transform(test_X)

    train_X = train_X.reshape(train_X.shape[0], lag, (train_X.shape[1] // lag))
    test_X = test_X.reshape(test_X.shape[0], lag, (test_X.shape[1] // lag))

    model = Sequential([
        Input((train_X.shape[1], train_X.shape[2],)),
        LSTM(64, activation="relu", return_sequences=True),
        LSTM(32, activation="relu"),
        Dense(1, activation="linear")
    ])

    model.compile(
        loss="mean_squared_error",
        optimizer="adam",
        metrics=["mean_squared_error"],
    )

    history = model.fit(train_X, train_y, validation_split=0.2, epochs=32, batch_size=8)

    sns.lineplot(history.history['loss'][1:], label='loss')
    sns.lineplot(history.history['val_loss'][1:], label='val_loss')
    plt.show()

    pred_y = model.predict(test_X)
    print(r2_score(test_y, pred_y))
