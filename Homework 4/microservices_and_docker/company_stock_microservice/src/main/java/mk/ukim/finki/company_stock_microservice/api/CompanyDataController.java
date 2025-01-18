package mk.ukim.finki.company_stock_microservice.api;

import mk.ukim.finki.company_stock_microservice.model.Observation;
import mk.ukim.finki.company_stock_microservice.service.StockService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stockdata")
public class CompanyDataController {

    private final StockService stockService;

    public CompanyDataController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    public ResponseEntity<List<Observation>> filterData(
            @RequestParam
            String company,
            @RequestParam (required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate dateFrom,
            @RequestParam
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate dateTo
    ) {
        return ResponseEntity.ok(stockService.getRecordsFromTo(company, dateFrom, dateTo));
    }
}
