package mk.finki.ukim.mk.stock_exchange_analyst.service;


import mk.finki.ukim.mk.stock_exchange_analyst.model.Sentiment;

import java.util.List;

public interface SentimentService {

    Sentiment    getSentimentOf(String company);
    List<String> getFullCompanyNames();

}
