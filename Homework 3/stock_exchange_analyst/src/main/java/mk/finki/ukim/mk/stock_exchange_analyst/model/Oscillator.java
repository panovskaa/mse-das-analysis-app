package mk.finki.ukim.mk.stock_exchange_analyst.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

@Data
@AllArgsConstructor
public class Oscillator {
    private String                   name;
    private Double                   value;
    private Function<Double, String> indicator;

    private static NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);

    public String indicates() {
        return indicator.apply(value);
    }

    public String getFormattedNum(Object num) {
        if (num == null) {
            return "";
        }
        Number n = (Number) num;
        return format.format(n);
    }
}
