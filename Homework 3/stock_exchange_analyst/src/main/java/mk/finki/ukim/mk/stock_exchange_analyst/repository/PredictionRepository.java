package mk.finki.ukim.mk.stock_exchange_analyst.repository;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.mk.stock_exchange_analyst.model.Prediction;
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
import java.util.*;

@Repository
public class PredictionRepository {

    @Value("${database.pred.path}")
    private String predPath;

    private final Map<String, List<Prediction>> companies;

    public PredictionRepository() {
        companies = new HashMap<>();
    }

    @PostConstruct
    public void update() {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(predPath))) {
            for (Path predCSV : stream) {

                List<List<String>> companyData = readCSV(predCSV.toString());
                List<String> predStrings = companyData.get(1);

                System.out.println(predCSV);
                long size = predCSV.toString().split("/").size();
                String companyName = predCSV.toString().split("/")[size-1].split("\\.")[0].split("_")[0];

                List<Prediction> predictions = createPredictions(predStrings);

                companies.put(companyName, predictions);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Prediction> getPredictionsOf(String company) {
        if (!companies.containsKey(company)) {
            return null;
        }
        return companies.get(company);
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

    private List<Prediction> createPredictions(List<String> ls) {
        LocalDate today = LocalDate.now();
        List<Prediction> retval = new ArrayList<>();
        for (int i = 1; i <= ls.size(); i++) {
            retval.add(new Prediction(Double.parseDouble(ls.get(i-1)), today.plusDays(i)));
        }
        return retval;
    }
}