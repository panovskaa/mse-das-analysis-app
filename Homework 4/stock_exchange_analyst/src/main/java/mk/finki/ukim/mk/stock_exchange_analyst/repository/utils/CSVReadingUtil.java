package mk.finki.ukim.mk.stock_exchange_analyst.repository.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReadingUtil {

    public static List<List<String>> readCSV(String company) throws IOException {
        List<List<String>> records = new ArrayList<>();
        try {

            FileReader reader = new FileReader(String.format("%s", company));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            for (CSVRecord csvRecord : csvParser) {
                List<String> row = new ArrayList<>();
                csvRecord.forEach(row::add);
                records.add(row);
            }

        } catch (IOException e) {
            throw new IOException(e);
        }

        return records;
    }

    private CSVReadingUtil() {}

}
