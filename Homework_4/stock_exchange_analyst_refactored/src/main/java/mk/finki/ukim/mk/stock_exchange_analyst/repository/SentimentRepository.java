package mk.finki.ukim.mk.stock_exchange_analyst.repository;

import mk.finki.ukim.mk.stock_exchange_analyst.model.Sentiment;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.utils.CSVReadingUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public void update() throws IOException {
        List<List<String>> companyData = CSVReadingUtil.readCSV(predPath);
        List<List<String>> sentimentStrs = companyData.subList(1, companyData.size());

        constructSentimentTable(sentimentStrs);
    }

    public List<String> getFullCompanyNames() {
        return companies.keySet().stream().sorted().collect(Collectors.toList());
    }

    public Sentiment getSentimentOf(String companyName) {
        return companies.get(companyName);
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
