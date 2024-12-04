package mk.finki.ukim.mk.stock_exchange_analyst.service.impl;

import mk.finki.ukim.mk.stock_exchange_analyst.repository.StockRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.service.DatabaseRefreshService;
import org.springframework.stereotype.Service;

import java.text.ParseException;

@Service
public class DatabaseRefreshServiceImpl implements DatabaseRefreshService {

    private final StockRepository stockRepository;

    public DatabaseRefreshServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void refreshDatabase() {
        this.stockRepository.update();
    }

}
