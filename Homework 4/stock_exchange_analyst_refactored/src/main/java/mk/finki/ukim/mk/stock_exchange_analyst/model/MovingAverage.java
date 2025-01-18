package mk.finki.ukim.mk.stock_exchange_analyst.model;

import java.util.function.BiFunction;

public class MovingAverage extends Indicator{
    private Double                             price;
    private BiFunction<Double, Double, String> indicator;

    public MovingAverage(String name, Double price, Double value, BiFunction<Double, Double, String> indicator) {
        super(name, value);
        this.price = price;
        this.indicator = indicator;
    }

    @Override
    public String indicates() {
        return indicator.apply(price, value);
    }

}
