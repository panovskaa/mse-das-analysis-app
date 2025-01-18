package mk.ukim.finki.company_stock_microservice.service.impl;

import mk.ukim.finki.company_stock_microservice.model.Observation;
import mk.ukim.finki.company_stock_microservice.repository.StockRepository;
import mk.ukim.finki.company_stock_microservice.service.StockService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Observation> getRecordsFromTo(String company, LocalDate from, LocalDate to) {
        return stockRepository.getRecordsFromTo(company, from, to);
    }

}