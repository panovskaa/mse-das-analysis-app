package mk.finki.ukim.mk.stock_exchange_analyst.service.impl;

import mk.finki.ukim.mk.stock_exchange_analyst.repository.PredictionRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.SentimentRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.StockRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.TAPointRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.service.DatabaseRefreshService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DatabaseRefreshServiceImpl implements DatabaseRefreshService {

    private final StockRepository stockRepository;
    private final TAPointRepository taPointRepository;
    private final PredictionRepository predictionRepository;
    private final SentimentRepository sentimentRepository;

    public DatabaseRefreshServiceImpl(StockRepository stockRepository, TAPointRepository taPointRepository, PredictionRepository predictionRepository, SentimentRepository sentimentRepository) {
        this.stockRepository = stockRepository;
        this.taPointRepository = taPointRepository;
        this.predictionRepository = predictionRepository;
        this.sentimentRepository = sentimentRepository;
    }

    @Override
    public void refreshDatabase() throws IOException {
        this.stockRepository.update();
        this.taPointRepository.update();
        this.predictionRepository.update();
        this.sentimentRepository.update();
    }
}
