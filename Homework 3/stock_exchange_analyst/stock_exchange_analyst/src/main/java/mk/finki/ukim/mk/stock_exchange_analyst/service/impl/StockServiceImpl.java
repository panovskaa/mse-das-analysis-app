package mk.finki.ukim.mk.stock_exchange_analyst.service.impl;

import mk.finki.ukim.mk.stock_exchange_analyst.model.Observation;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.StockRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.service.StockService;
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
        return stockRepository.getRecordsFromTo(company,from, to);
    }

    @Override
    public List<String> listCompanies() {
        return stockRepository.listCompanies();
    }
}
