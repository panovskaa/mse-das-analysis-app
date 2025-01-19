package mk.ukim.finki.company_stock_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableDiscoveryClient
public class CompanyStockMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyStockMicroserviceApplication.class, args);
    }

}
