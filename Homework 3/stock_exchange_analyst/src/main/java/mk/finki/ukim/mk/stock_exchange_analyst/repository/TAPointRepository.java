package mk.finki.ukim.mk.stock_exchange_analyst.repository;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.mk.stock_exchange_analyst.model.TAPoint;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class TAPointRepository {

    @Value("${database.ta.path}")
    private String taPath;

    private final Map<String, List<TAPoint>> companies;

    public TAPointRepository() {
        companies = new HashMap<>();
    }

    @PostConstruct
    public void update() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(taPath))) {
            for (Path analysisCSV : stream) {

                List<List<String>> indicators = readCSV(analysisCSV.toString());
                indicators = indicators.subList(1, indicators.size());

                int size = analysisCSV.toString().split("/").size();

                String companyName = analysisCSV.toString().split("/")[size-1].split("\\.")[0].split("_")[0];

                List<TAPoint> taPoints = indicators.stream()
                        .map(this::createTAPoint)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

                companies.put(companyName, taPoints);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Optional<TAPoint> latest(String company) {
        if (!companies.containsKey(company)) return Optional.empty();

        return companies.get(company)
                .stream()
                .max(Comparator.comparing(TAPoint::getDate));
    }


    private List<List<String>> readCSV(String company) {
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
            e.printStackTrace();
        }

        return records;
    }

    private Optional<TAPoint> createTAPoint(List<String> ls) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {

            LocalDate date = LocalDate.parse(ls.get(0), dtf);
            Double price = !ls.get(1).trim().isEmpty() ? Double.parseDouble(ls.get(1)) : null;
            Double RSI = !ls.get(4).trim().isEmpty() ? Double.parseDouble(ls.get(4)) : null;
            Double KSTOCH = !ls.get(5).trim().isEmpty() ? Double.parseDouble(ls.get(5)) : null;
            Double DSTOCH = !ls.get(6).trim().isEmpty() ? Double.parseDouble(ls.get(6)) : null;
            Double ROC = !ls.get(7).trim().isEmpty() ? Double.parseDouble(ls.get(7)) : null;
            Double MOMENTUM = !ls.get(8).trim().isEmpty() ? Double.parseDouble(ls.get(8)) : null;
            Double WILLIAMS = !ls.get(9).trim().isEmpty() ? Double.parseDouble(ls.get(9)) : null;
            Double sma1 = !ls.get(10).trim().isEmpty() ? Double.parseDouble(ls.get(10)) : null;
            Double sma7 = !ls.get(11).trim().isEmpty() ? Double.parseDouble(ls.get(11)) : null;
            Double sma30 = !ls.get(12).trim().isEmpty() ? Double.parseDouble(ls.get(12)) : null;
            Double ema1 = !ls.get(13).trim().isEmpty() ? Double.parseDouble(ls.get(13)) : null;
            Double ema7 = !ls.get(14).trim().isEmpty() ? Double.parseDouble(ls.get(14)) : null;
            Double ema30 = !ls.get(15).trim().isEmpty() ? Double.parseDouble(ls.get(15)) : null;
            Double wma1 = !ls.get(16).trim().isEmpty() ? Double.parseDouble(ls.get(16)) : null;
            Double wma7 = !ls.get(17).trim().isEmpty() ? Double.parseDouble(ls.get(17)) : null;
            Double wma30 = !ls.get(18).trim().isEmpty() ? Double.parseDouble(ls.get(18)) : null;
            Double dema1 = !ls.get(19).trim().isEmpty() ? Double.parseDouble(ls.get(19)) : null;
            Double dema7 = !ls.get(20).trim().isEmpty() ? Double.parseDouble(ls.get(20)) : null;
            Double dema30 = !ls.get(21).trim().isEmpty() ? Double.parseDouble(ls.get(21)) : null;
            Double tema1 = !ls.get(22).trim().isEmpty() ? Double.parseDouble(ls.get(22)) : null;
            Double tema7 = !ls.get(23).trim().isEmpty() ? Double.parseDouble(ls.get(23)) : null;
            Double tema30 = !ls.get(24).trim().isEmpty() ? Double.parseDouble(ls.get(24)) : null;

            return Optional.of(
                    new TAPoint
                            (date, price, RSI, KSTOCH, DSTOCH, ROC, MOMENTUM, WILLIAMS, sma1, sma7, sma30, ema1, ema7,
                                    ema30, wma1, wma7, wma30, dema1, dema7, dema30, tema1, tema7, tema30)
            );

        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}