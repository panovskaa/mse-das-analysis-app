package mk.ukim.finki.company_stock_microservice.model;

import mk.ukim.finki.company_stock_microservice.model.util.FormatConvenienceUtil;

import java.time.LocalDate;
import java.util.Map;

public class Observation implements Comparable<Observation> {

    private final LocalDate date;
    private final Map<String, Double> values;

    public Observation(LocalDate date, Map<String, Double> values) {
        this.date = date;
        this.values = values;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<String, Double> getValues() {
        return values;
    }

    @Override
    public int compareTo(Observation o) {
        return date.compareTo(o.date);
    }

    public String getFormattedNum(Object num) {
        if (num == null) {
            return "";
        }
        Number n = (Number) num;
        return FormatConvenienceUtil.numberFormat.format(n);
    }
}
