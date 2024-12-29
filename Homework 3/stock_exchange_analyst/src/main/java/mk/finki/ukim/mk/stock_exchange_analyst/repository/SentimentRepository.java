package mk.finki.ukim.mk.stock_exchange_analyst.repository;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.mk.stock_exchange_analyst.model.Prediction;
import mk.finki.ukim.mk.stock_exchange_analyst.model.Sentiment;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class SentimentRepository {

    @Value("${database.sentiment.path}")
    private String predPath;

    private final Map<String, Sentiment> companies;

    public SentimentRepository() {
        companies = new HashMap<>();
    }

    public void update() {

        List<List<String>> companyData = readCSV(predPath);
        List<List<String>> sentimentStrs = companyData.subList(1, companyData.size());

        constructSentimentTable(sentimentStrs);
    }

    public List<String> getFullCompanyNames() {
        return companies.keySet().stream().sorted().collect(Collectors.toList());
    }

    public Sentiment getSentimentOf(String companyName) {
        return companies.get(companyName);
    }

    private List<List<String>> readCSV(String file) {
        List<List<String>> records = new ArrayList<>();
        try {
            FileReader reader = new FileReader(String.format("%s", file));
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

    private void constructSentimentTable(List<List<String>> ls) {

        for (List<String> row : ls) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (row.get(5).isEmpty() || row.get(6).isEmpty()) continue;
            LocalDateTime date = LocalDateTime.parse(row.get(2), formatter);
            Double score = Double.parseDouble(row.get(6));
            String sentiment = row.get(5);

            companies.put(row.get(4), new Sentiment(score, sentiment, date.toLocalDate()));

        }

    }
}
