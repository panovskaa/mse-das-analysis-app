package mk.finki.ukim.mk.stock_exchange_analyst.model;

import lombok.Data;

import java.util.function.Function;

public class Oscillator extends Indicator {
    private Function<Double, String> indicator;

    public Oscillator(String name, Double value, Function<Double, String> indicator) {
        super(name, value);
        this.indicator = indicator;
    }

    @Override
    public String indicates() {
        return indicator.apply(value);
    }
}
