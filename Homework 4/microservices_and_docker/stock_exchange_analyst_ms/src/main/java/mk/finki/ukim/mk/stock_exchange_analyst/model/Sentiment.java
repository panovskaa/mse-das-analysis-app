package mk.finki.ukim.mk.stock_exchange_analyst.model;

import lombok.Data;
import mk.finki.ukim.mk.stock_exchange_analyst.model.utils.FormatConvenienceUtil;

import java.time.LocalDate;

@Data
public class Sentiment {

    public Sentiment(Double score, String sentiment, LocalDate lastReport) {
        this.score = score;
        this.sentiment = sentiment;
        this.lastReportDate = lastReport;
    }

    private Double    score;
    private String    sentiment;
    private LocalDate lastReportDate;

    public Double getScore() {
        return score;
    }

    public String getSentiment() {
        return sentiment;
    }

    public LocalDate getLastReport() {
        return lastReportDate;
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
