package mk.finki.ukim.mk.stock_exchange_analyst.model.utils.factory;

import mk.finki.ukim.mk.stock_exchange_analyst.model.MovingAverage;
import mk.finki.ukim.mk.stock_exchange_analyst.model.Oscillator;

import java.util.function.BiFunction;
import java.util.function.Function;

public class IndicatorFactory {

    public static Oscillator createOscillator(String name, Double value, Function<Double, String> indicator) {
        return new Oscillator(name, value, indicator);
    }

    public static MovingAverage createMovingAverage(String name, Double price, Double value, BiFunction<Double, Double, String> indicator) {
        return new MovingAverage(name, price, value, indicator);
    }

    // block object create
    private IndicatorFactory() {}

}
