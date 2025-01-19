package mk.finki.ukim.mk.stock_exchange_analyst.service;

import mk.finki.ukim.mk.stock_exchange_analyst.model.Observation;

import java.time.LocalDate;
import java.util.List;

public interface StockService {

    List<Observation> getRecordsFromTo(String company, LocalDate from, LocalDate to);
    List<String> listCompanies();

}
