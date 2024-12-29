package mk.finki.ukim.mk.stock_exchange_analyst.service;

import mk.finki.ukim.mk.stock_exchange_analyst.model.MovingAverage;
import mk.finki.ukim.mk.stock_exchange_analyst.model.Oscillator;
import mk.finki.ukim.mk.stock_exchange_analyst.model.TAPoint;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.TAPointRepository;

import java.util.List;
import java.util.Map;

public interface TAService {
    TAPoint getLatest(String company);
    List<Oscillator> getLatestOscillators(String company);
    List<MovingAverage> getLatestMAs(String company);
    public Map<String, Long> oscillatorSummary(String company);
    public Map<String, Long> MASummary(String company);
}
