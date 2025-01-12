package mk.finki.ukim.mk.stock_exchange_analyst.repository;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.mk.stock_exchange_analyst.model.TAPoint;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.utils.CSVReadingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

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
    public void update() throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(taPath))) {
            for (Path analysisCSV : stream) {

                List<List<String>> indicators = CSVReadingUtil.readCSV(analysisCSV.toString());
                indicators = indicators.subList(1, indicators.size());

                String companyName = analysisCSV.toString().split("\\\\")[4].split("\\.")[0].split("_")[0];

                List<TAPoint> taPoints = indicators.stream()
                        .map(this::createTAPoint)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

                companies.put(companyName, taPoints);

            }
        }
    }

    public Optional<TAPoint> latest(String company) {
        if (!companies.containsKey(company)) return Optional.empty();

        return companies.get(company)
                .stream()
                .max(Comparator.comparing(TAPoint::getDate));
    }

    private Optional<TAPoint> createTAPoint(List<String> ls) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate date = LocalDate.parse(ls.get(0), dtf);
            Double price = !ls.get(1).trim().isEmpty() ? Double.parseDouble(ls.get(1)) : null;

            Map<String, Double> indicators = getStringDoubleMap(ls);

            return Optional.of(new TAPoint(date, price, indicators));
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, Double> getStringDoubleMap(List<String> ls) {
        Map<String, Double> indicators = new HashMap<>();
        String[] indicatorsInOrder = {
                "RSI", "KSTOCH", "DSTOCH", "ROC", "MOMENTUM", "WILLIAMS", "sma1", "sma7", "sma30", "ema1", "ema7",
                "ema30", "wma1", "wma7", "wma30", "dema1", "dema7", "dema30", "tema1", "tema7", "tema30"};
        for (int i = 4, j = 0; j < indicatorsInOrder.length; i++, j++) {
            // indexing the csv from 4, and indexing the indicator names above from 0
            if (!ls.get(i).trim().isEmpty()) {
                indicators.put(
                        indicatorsInOrder[j],
                        Double.parseDouble(ls.get(i))
                );
            }
        }
        return indicators;
    }
}
