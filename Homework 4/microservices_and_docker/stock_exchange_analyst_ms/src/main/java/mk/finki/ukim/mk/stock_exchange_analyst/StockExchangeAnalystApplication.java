package mk.finki.ukim.mk.stock_exchange_analyst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
public class StockExchangeAnalystApplication {

    public static void main(String[] args) throws IOException, InterruptedException {

         SpringApplication.run(StockExchangeAnalystApplication.class, args);

    }

}
