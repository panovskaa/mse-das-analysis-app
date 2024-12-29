package mk.finki.ukim.mk.stock_exchange_analyst.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class TAPoint {

    private LocalDate date;
    private Double    price;
    private Double    RSI;
    private Double    KSTOCH;
    private Double    DSTOCH;
    private Double    ROC;
    private Double    MOMENTUM;
    private Double    WILLIAMS;
    private Double    sma1;
    private Double    sma7;
    private Double    sma30;
    private Double    ema1;
    private Double    ema7;
    private Double    ema30;
    private Double    wma1;
    private Double    wma7;
    private Double    wma30;
    private Double    dema1;
    private Double    dema7;
    private Double    dema30;
    private Double    tema1;
    private Double    tema7;
    private Double    tema30;

    public List<Oscillator> oscillators() {
        return List.of(
                new Oscillator("RSI", RSI, (RSI) -> {
                    if (RSI < 30) {
                        return "Buy";
                    } else if (RSI > 70) {
                        return "Sell";
                    }
                    return "Neutral";
                }),
                new Oscillator("K-STOCH", KSTOCH, (KSTOCH) -> {
                    if (KSTOCH != null) {
                        if (KSTOCH < 20) {
                            return "Buy";
                        } else if (KSTOCH > 80) {
                            return "Sell";
                        }
                    }
                    return "Neutral";
                }),
                new Oscillator("D-STOCH", DSTOCH, (DSTOCH) -> {
                    if (DSTOCH != null) {
                        if (DSTOCH < 20) {
                            return "Buy";
                        } else if (DSTOCH > 80) {
                            return "Sell";
                        }
                    }
                    return "Neutral";
                }),
                new Oscillator("Rate of Change", ROC, (ROC) -> {
                    if (ROC < -1) {
                        return "Sell";
                    } else if (ROC > 1) {
                        return "Buy";
                    }
                    return "Neutral";
                }),
                new Oscillator("Momentum", MOMENTUM, (MOMENTUM) -> {
                    if (MOMENTUM < -1) {
                        return "Sell";
                    } else if (MOMENTUM > 1) {
                        return "Buy";
                    }
                    return "Neutral";
                }),
                new Oscillator("Williams %R", WILLIAMS, (WILLIAMS) -> {
                    if (WILLIAMS != null) {
                        if (WILLIAMS < -80) {
                            return "Buy";
                        } else if (WILLIAMS > -20) {
                            return "Sell";
                        }
                    }
                    return "Neutral";
                })
        );
    }

    public List<MovingAverage> MAs() {

        Double THRESHOLD = 0.01;

        return List.of(
                new MovingAverage("Simple Moving Average (1)", price, sma1, (price, sma1) -> {
                    if (Math.abs(price - sma1) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > sma1 ? "Buy" : "Sell";
                }),
                new MovingAverage("Simple Moving Average (7)", price, sma7, (price, sma7) -> {
                    if (Math.abs(price - sma7) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > sma7 ? "Buy" : "Sell";
                }),
                new MovingAverage("Simple Moving Average (30)", price, sma30, (price, sma30) -> {
                    if (Math.abs(price - sma30) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > sma30 ? "Buy" : "Sell";
                }),
                new MovingAverage("Exponential Moving Average (1)", price, ema1, (price, ema1) -> {
                    if (Math.abs(price - ema1) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > ema1 ? "Buy" : "Sell";
                }),
                new MovingAverage("Exponential Moving Average (7)", price, ema7, (price, ema7) -> {
                    if (Math.abs(price - ema7) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > ema7 ? "Buy" : "Sell";
                }),
                new MovingAverage("Exponential Moving Average (30)", price, ema30, (price, ema30) -> {
                    if (Math.abs(price - ema30) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > ema30 ? "Buy" : "Sell";
                }),
                new MovingAverage("Weighted Moving Average (1)", price, wma1, (price, wma1) -> {
                    if (Math.abs(price - wma1) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > wma1 ? "Buy" : "Sell";
                }),
                new MovingAverage("Weighted Moving Average (7)", price, wma7, (price, wma7) -> {
                    if (Math.abs(price - wma7) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > wma7 ? "Buy" : "Sell";
                }),
                new MovingAverage("Weighted Moving Average (30)", price, wma30, (price, wma30) -> {
                    if (Math.abs(price - wma30) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > wma30 ? "Buy" : "Sell";
                }),
                new MovingAverage("Double Exponential Moving Average (1)", price, dema1, (price, dema1) -> {
                    if (Math.abs(price - dema1) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > dema1 ? "Buy" : "Sell";
                }),
                new MovingAverage("Double Exponential Moving Average (7)", price, dema7, (price, dema7) -> {
                    if (Math.abs(price - dema7) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > dema7 ? "Buy" : "Sell";
                }),
                new MovingAverage("Double Exponential Moving Average (30)", price, dema30, (price, dema30) -> {
                    if (Math.abs(price - dema30) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > dema30 ? "Buy" : "Sell";
                }),
                new MovingAverage("Triple Exponential Moving Average (1)", price, tema1, (price, tema1) -> {
                    if (Math.abs(price - tema1) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > tema1 ? "Buy" : "Sell";
                }),
                new MovingAverage("Triple Exponential Moving Average (7)", price, tema7, (price, tema7) -> {
                    if (Math.abs(price - tema7) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > tema7 ? "Buy" : "Sell";
                }),
                new MovingAverage("Triple Exponential Moving Average (30)", price, tema30, (price, tema30) -> {
                    if (Math.abs(price - tema30) < price * THRESHOLD) {
                        return "Neutral";
                    }
                    return price > tema30 ? "Buy" : "Sell";
                })

        );
    }

    public Map<String, Long> oscillatorSummary() {
        return oscillators().stream().collect(Collectors.groupingBy(Oscillator::indicates, Collectors.counting()));
    }

    public Map<String, Long> movingAverageSummary() {
        return MAs().stream().collect(Collectors.groupingBy(MovingAverage::indicates, Collectors.counting()));
    }
}
