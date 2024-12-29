package mk.finki.ukim.mk.stock_exchange_analyst.tasks;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.mk.stock_exchange_analyst.service.DatabaseRefreshService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
public class DataFetcher {

    @Value("${database.update.path}")
    private String scriptRun;

    private final DatabaseRefreshService refreshService;

    public DataFetcher(DatabaseRefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @Scheduled(cron = "0 0 0 * * *") // Update the database at midnight
    @PostConstruct
    public void updateDatabase() throws InterruptedException {
        String[] fillDB = {"python", "src/main/resources/python-scripts/parallel_filters.py"};
        String[] makeAnalysis = {"python", "src/main/resources/python-scripts/technical_analysis.py"};
        String[] predict = {"python", "src/main/resources/python-scripts/lstm.py"};
        ProcessBuilder pbFillDb = new ProcessBuilder(fillDB);
        ProcessBuilder pbMakeAnalysis = new ProcessBuilder(makeAnalysis);
        ProcessBuilder pbPredicting = new ProcessBuilder(predict);

        try {

            System.out.println("\033[0;34m" + String.format("================%10s DATABASE UPDATE %10s================", " ", " ") + "\033[0m");

            Process filling = pbFillDb.start();

            filling.waitFor();
            System.out.println("\033[0;32m" + String.format("%41s", "UPDATE SUCCESS") + "\033[0m");
            System.out.println("\033[0;34m" + String.format("================%8s TECHNICAL ANALYSIS %9s================", " ", " ") + "\033[0m");

            Process analysis = pbMakeAnalysis.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(analysis.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    System.out.println("\033[0;32m\t" + String.format("\t\t\t%s -> %30s", parts[0], parts[1]));
                }
            }

            analysis.waitFor();

            System.out.println("\033[0;34m" + String.format("================%6s MODEL PRICE PREDICTION %6s================", " ", " ") + "\033[0m");

            Process predicting = pbPredicting.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(predicting.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            predicting.waitFor();
            refreshService.refreshDatabase();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
