package mk.finki.ukim.mk.stock_exchange_analyst.model.utils.strategy;

import java.util.function.BiFunction;
import java.util.function.Function;

public class IndicatorStrategies {

    public static Function<Double, String> rsi() {
        return value -> {
            if (value < 30) {
                return "Buy";
            } else if (value > 70) {
                return "Sell";
            }
            return "Neutral";
        };
    }

    public static Function<Double, String> kStoch() {
        return value -> {
            if (value != null) {
                if (value < 20) {
                    return "Buy";
                } else if (value > 80) {
                    return "Sell";
                }
            }
            return "Neutral";
        };
    }

    public static Function<Double, String> dStoch() {
        return value -> {
            if (value != null) {
                if (value < 20) {
                    return "Buy";
                } else if (value > 80) {
                    return "Sell";
                }
            }
            return "Neutral";
        };
    }

    public static Function<Double, String> roc() {
        return value -> {
            if (value < -1) {
                return "Sell";
            } else if (value > 1) {
                return "Buy";
            }
            return "Neutral";
        };
    }

    public static Function<Double, String> momentum() {
        return value -> {
            if (value < -1) {
                return "Sell";
            } else if (value > 1) {
                return "Buy";
            }
            return "Neutral";
        };
    }

    public static Function<Double, String> williams() {
        return value -> {
            if (value != null) {
                if (value < -80) {
                    return "Buy";
                } else if (value > -20) {
                    return "Sell";
                }
            }
            return "Neutral";
        };
    }

    public static BiFunction<Double, Double, String> MAStrategy(double threshold) {
        return (price, MAVal) -> {
            if (Math.abs(price - MAVal) < price * threshold) {
                return "Neutral";
            }
            return price > MAVal ? "Buy" : "Sell";
        };
    }

    // block object create
    private IndicatorStrategies() {}
}
