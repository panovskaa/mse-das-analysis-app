package mk.finki.ukim.mk.stock_exchange_analyst.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import mk.finki.ukim.mk.stock_exchange_analyst.model.utils.FormatConvenienceUtil;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Prediction {

    private final Double    price;
    private final LocalDate date;

    public String getFormattedNum(Object num) {
        if (num == null) {
            return "";
        }
        Number n = (Number) num;
        return FormatConvenienceUtil.numberFormat.format(n);
    }

    public String getFormattedDate(Object date) {
        if (date == null) {
            return "";
        }
        LocalDate d = (LocalDate) date;
        return d.format(FormatConvenienceUtil.dateTimeFormatter);
    }

}
