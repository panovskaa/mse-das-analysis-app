package mk.finki.ukim.mk.stock_exchange_analyst.model;

import lombok.Data;
import mk.finki.ukim.mk.stock_exchange_analyst.model.utils.FormatConvenienceUtil;

import java.time.LocalDate;

@Data
public class Prediction {

    private final Double    price;
    private final LocalDate date;

    public Prediction(Double price, LocalDate date) {
        this.price = price;
        this.date = date;
    }

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
