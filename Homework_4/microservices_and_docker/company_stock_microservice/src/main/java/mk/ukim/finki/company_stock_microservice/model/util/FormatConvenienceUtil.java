package mk.ukim.finki.company_stock_microservice.model.util;

import lombok.Data;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Data
public class FormatConvenienceUtil {

    public static final NumberFormat numberFormat = NumberFormat.getInstance(Locale.GERMAN);

    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

    // block object create
    private FormatConvenienceUtil() {}

}
