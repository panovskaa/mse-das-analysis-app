package mk.finki.ukim.mk.stock_exchange_analyst.service;

import mk.finki.ukim.mk.stock_exchange_analyst.model.Prediction;

import java.util.List;
import java.util.Optional;

public interface PredictionService {

    List<Prediction> getPredictionsOf(String company);

}
