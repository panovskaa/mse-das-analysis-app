package mk.finki.ukim.mk.stock_exchange_analyst.service.impl;

import mk.finki.ukim.mk.stock_exchange_analyst.model.Prediction;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.PredictionRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.SentimentRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.service.PredictionService;
import mk.finki.ukim.mk.stock_exchange_analyst.service.exception.NoSuchCompanyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionServiceImpl implements PredictionService {

    public final PredictionRepository predictionRepository;

    public PredictionServiceImpl(PredictionRepository predictionRepository) {
        this.predictionRepository = predictionRepository;
    }

    @Override
    public List<Prediction> getPredictionsOf(String company) {
        if (predictionRepository.getPredictionsOf(company) == null) {
            throw new NoSuchCompanyException("Predictions not found, insufficient data for company '" + company + "'");
        }
        return predictionRepository.getPredictionsOf(company);
    }
}
