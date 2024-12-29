package mk.finki.ukim.mk.stock_exchange_analyst.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
@AllArgsConstructor
public class Prediction {

    private final Double price;
    private final LocalDate date;

    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy");
    private final static NumberFormat format = NumberFormat.getInstance(Locale.GERMAN);

    public String getFormattedNum(Object num) {
        if (num == null) {
            return "";
        }
        Number n = (Number) num;
        return format.format(n);
    }

    public String getFormattedDate(Object date) {
        if (date == null) {
            return "";
        }
        return dtf.format((LocalDate) date);
    }

}
