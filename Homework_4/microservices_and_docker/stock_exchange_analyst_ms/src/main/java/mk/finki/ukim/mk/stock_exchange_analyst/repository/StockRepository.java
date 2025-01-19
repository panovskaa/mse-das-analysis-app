package mk.finki.ukim.mk.stock_exchange_analyst.repository;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.mk.stock_exchange_analyst.model.Observation;
import mk.finki.ukim.mk.stock_exchange_analyst.model.utils.FormatConvenienceUtil;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.utils.CSVReadingUtil;
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class StockRepository {

    @Value("${database.data.path}")
    private String dbPath;

    private final Map<String, List<Observation>> companies = new HashMap<>();

    @PostConstruct
    public void update() throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dbPath))) {
            for (Path companyCSV : stream) {
                List<List<String>> companyData = CSVReadingUtil.readCSV(companyCSV.toString());
                companyData = companyData.subList(1, companyData.size());

                String companyName = companyCSV.getFileName().toString().split("\\.")[0];

                List<Observation> observations = companyData.stream()
                        .map(this::createObservation)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

                companies.put(companyName, observations);
            }
        }
    }

    public List<Observation> getRecordsFromTo(String company, LocalDate from, LocalDate to) {
        return companies.get(company)
                .stream()
                .filter(obs -> obs.getDate().isBefore(to.plusDays(1))
                        &&
                        obs.getDate().isAfter(from.minusDays(1)))
                .sorted()
                .toList();
    }

    public List<String> listCompanies() {
        return companies.keySet()
                .stream()
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    private Optional<Observation> createObservation(List<String> ls) {

        try {
            LocalDate date = LocalDate.parse(ls.get(0), FormatConvenienceUtil.dateTimeFormatter);
            Map<String, Double> values = new HashMap<>();
            String[] valueNames = {"lastTradePrice", "max", "min", "avgPrice", "chg", "volume", "turnoverBestMKD", "totalTurnoverMKD"};
            for (int i = 0; i < valueNames.length; i++) {
                if (!ls.get(i+1).trim().isBlank()) {
                    values.put(
                            valueNames[i],
                            FormatConvenienceUtil.numberFormat.parse(ls.get(i+1)).doubleValue()
                    );
                }
            }
            return Optional.of(new Observation(date, values));
        } catch (ParseException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
