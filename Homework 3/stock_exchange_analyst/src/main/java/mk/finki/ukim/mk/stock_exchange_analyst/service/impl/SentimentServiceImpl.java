package mk.finki.ukim.mk.stock_exchange_analyst.service.impl;

import mk.finki.ukim.mk.stock_exchange_analyst.model.Sentiment;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.SentimentRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.service.SentimentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SentimentServiceImpl implements SentimentService {

    private final SentimentRepository sentimentRepository;

    public SentimentServiceImpl(SentimentRepository sentimentRepository) {
        this.sentimentRepository = sentimentRepository;
    }

    @Override
    public Sentiment getSentimentOf(String company) {
        return sentimentRepository.getSentimentOf(company);
    }

    @Override
    public List<String> getFullCompanyNames() {
        return sentimentRepository.getFullCompanyNames();
    }
}
