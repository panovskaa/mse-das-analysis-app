package mk.finki.ukim.mk.stock_exchange_analyst.service;

import mk.finki.ukim.mk.stock_exchange_analyst.model.TAPoint;
import java.util.Map;

public interface TAService {
    TAPoint getLatest(String company);
    Map<String, Long> oscillatorSummary(String company);
    Map<String, Long> MASummary(String company);
}
