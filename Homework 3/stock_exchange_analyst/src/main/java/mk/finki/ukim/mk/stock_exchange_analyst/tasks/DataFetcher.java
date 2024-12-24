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

    @Scheduled(cron = "0 0 0 * * *") // Update the database at midnight, this is an active component
    @PostConstruct
    public void updateDatabase() throws InterruptedException {

        String[] command = {"python", "src/main/resources/python-scripts/parallel_filters.py"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);

        try {
            Process process = processBuilder.start();

            process.waitFor();

            refreshService.refreshDatabase();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}



