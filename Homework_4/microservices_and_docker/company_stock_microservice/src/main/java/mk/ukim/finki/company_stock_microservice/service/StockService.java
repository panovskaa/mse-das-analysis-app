package mk.ukim.finki.company_stock_microservice.service;

import mk.ukim.finki.company_stock_microservice.model.Observation;

import java.time.LocalDate;
import java.util.List;

public interface StockService {

    List<Observation> getRecordsFromTo(String company, LocalDate from, LocalDate to);

}
