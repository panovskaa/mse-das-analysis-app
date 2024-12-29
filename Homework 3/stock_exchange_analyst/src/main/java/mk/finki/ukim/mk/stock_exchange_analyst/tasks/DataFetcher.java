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
        ProcessBuilder pbFillDb = new ProcessBuilder(fillDB);
        ProcessBuilder pbMakeAnalysis = new ProcessBuilder(makeAnalysis);

        try {
            Process filling = pbFillDb.start();

            // Capture and print the output stream of the analysis process

            filling.waitFor();

            Process analysis = pbMakeAnalysis.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(analysis.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            analysis.waitFor();

            refreshService.refreshDatabase();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
