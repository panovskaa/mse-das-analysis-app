package mk.finki.ukim.mk.stock_exchange_analyst.service.impl;

import mk.finki.ukim.mk.stock_exchange_analyst.repository.PredictionRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.StockRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.TAPointRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.service.DatabaseRefreshService;
import org.springframework.stereotype.Service;

@Service
public class DatabaseRefreshServiceImpl implements DatabaseRefreshService {

    private final StockRepository stockRepository;
    private final TAPointRepository taPointRepository;
    private final PredictionRepository predictionRepository;

    public DatabaseRefreshServiceImpl(StockRepository stockRepository, TAPointRepository taPointRepository, PredictionRepository predictionRepository) {
        this.stockRepository = stockRepository;
        this.taPointRepository = taPointRepository;
        this.predictionRepository = predictionRepository;
    }

    @Override
    public void refreshDatabase() {
        this.stockRepository.update();
        this.taPointRepository.update();
        this.predictionRepository.update();
    }
}
