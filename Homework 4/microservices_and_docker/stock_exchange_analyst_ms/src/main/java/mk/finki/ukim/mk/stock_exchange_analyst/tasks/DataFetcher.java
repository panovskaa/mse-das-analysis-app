package mk.finki.ukim.mk.stock_exchange_analyst.tasks;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.mk.stock_exchange_analyst.service.DatabaseRefreshService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class DataFetcher {

    private static final Logger logger = LoggerFactory.getLogger(DataFetcher.class);

    private final DatabaseRefreshService refreshService;

    @Value("${script.dbupdate.path}")
    private String fillDbPath;

    @Value("${script.ta.path}")
    private String taPath;

    @Value("${script.sentiment.path}")
    private String faPath;

    @Value("${script.predict.path}")
    private String predictPath;

    public DataFetcher(DatabaseRefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @Scheduled(cron = "0 0 0 * * *") // Update the database at midnight
    @PostConstruct
    public void updateDatabase() throws ParseException, IOException {
//        executeScript("Database Update", fillDbPath);
//        executeScript("Technical Analysis", taPath);
//        executeScript("Fundamental Analysis", faPath);
//        executeScript("Model Price Prediction", predictPath);
        refreshService.refreshDatabase();
    }

    private void executeScript(String taskName, String scriptPath) {
        String[] command = {"python", scriptPath};
        ProcessBuilder pb = new ProcessBuilder(command);

        try {
            logger.info("========== {} START ==========", taskName.toUpperCase());

            Process process = pb.start();
            ExecutorService executor = Executors.newFixedThreadPool(2);

            executor.submit(() -> logProcessOutput(process.getInputStream(), "INFO"));
            executor.submit(() -> logProcessOutput(process.getErrorStream(), "ERROR"));

            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.MINUTES);

            process.waitFor();
            logger.info("========== {} SUCCESS ==========", taskName.toUpperCase());
        } catch (IOException | InterruptedException e) {
            logger.error("========== {} FAILED ==========", taskName.toUpperCase(), e);
        }
    }

    private void logProcessOutput(InputStream inputStream, String stream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if ("ERROR".equals(stream)) {
                    logger.error(line);
                } else {
                    logger.info(line);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading process output", e);
        }
    }
}
