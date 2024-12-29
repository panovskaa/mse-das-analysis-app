package mk.finki.ukim.mk.stock_exchange_analyst.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.function.BiFunction;

@Data
@AllArgsConstructor
public class MovingAverage {
    private String name;
    private Double price;
    private Double value;
    private BiFunction<Double, Double, String> indicator;

    private final static NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);

    public String indicates() {
        return indicator.apply(price, value);
    }

    public String getFormattedNum(Object num) {
        if (num == null) {
            return "";
        }
        Number n = (Number) num;
        return format.format(n);
    }

}
