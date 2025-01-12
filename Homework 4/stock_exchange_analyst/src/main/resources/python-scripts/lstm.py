import os
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.metrics import r2_score
from sklearn.preprocessing import MinMaxScaler
from keras.api.models import Sequential
from keras.api.layers import Input, LSTM, Dense
import numpy as np

def read_csv(company_name):
    path = str(company_name)
    dataset = pd.read_csv(path)
    dataset = dataset[dataset['LastTradePrice'].notna()]

    new_dataset = pd.DataFrame()

    new_dataset['Date'] = pd.to_datetime(dataset['Date'])
    prices = [float(price.split(',')[0].replace('.', '')) for price in dataset['LastTradePrice'] if price is not None]

    new_dataset['Price'] = pd.to_numeric(prices, errors='coerce')
    new_dataset = new_dataset.set_index('Date')

    return new_dataset

if __name__ == '__main__':

    root = 'src/main/resources'
    files = os.listdir(f'{root}/db')

    trainall = False # set to True if to train for all companies (only in PRODUCTION, otherwise takes very long)

    example = 4 # make predictions for only the first k companies
    c = 0

    for file in files:

        if not trainall and c == example:
            break

        name = file.split(".")[0]
        df = read_csv(f'{root}/db/{file}')
        df = df.head(365)
        lag = 14
        periods = range(lag, 0, -1)

        df = pd.concat([df, df.shift(periods=periods)], axis=1)
        df = df.dropna()

        if len(df) == 0:
            continue

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
            metrics=["r2_score"]
        )

        history = model.fit(train_X, train_y, validation_split=0.2, epochs=32, batch_size=8)

        forecast_horizon = 5
        future_predictions = []

        last_input = test_X[-1].reshape(1, test_X.shape[1], test_X.shape[2])

        pred_df = pd.DataFrame()

        for i in range(forecast_horizon):

            next_pred = model.predict(last_input)[0, 0]
            future_predictions.append(next_pred)

            new_input = np.roll(last_input, -1, axis=1)
            new_input[0, -1, :] = next_pred
            last_input = new_input

            pred_df[f'day_plus_{i+1}'] = [next_pred]

        pred_df.to_csv(f'src/main/resources/pred/{name}_pr.csv', index=False)
        c += 1
