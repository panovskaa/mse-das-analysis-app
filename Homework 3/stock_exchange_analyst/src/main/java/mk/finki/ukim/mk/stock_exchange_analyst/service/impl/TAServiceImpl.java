package mk.finki.ukim.mk.stock_exchange_analyst.service.impl;

import mk.finki.ukim.mk.stock_exchange_analyst.model.MovingAverage;
import mk.finki.ukim.mk.stock_exchange_analyst.model.Oscillator;
import mk.finki.ukim.mk.stock_exchange_analyst.model.TAPoint;
import mk.finki.ukim.mk.stock_exchange_analyst.repository.TAPointRepository;
import mk.finki.ukim.mk.stock_exchange_analyst.service.TAService;
import mk.finki.ukim.mk.stock_exchange_analyst.service.exception.NoSuchCompanyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TAServiceImpl implements TAService {

    // access to all company dates for scalability reasons
    private final TAPointRepository taPointRepository;

    public TAServiceImpl(TAPointRepository taPointRepository) {
        this.taPointRepository = taPointRepository;
    }

    @Override
    public TAPoint getLatest(String company) {
        return taPointRepository.latest(company).orElseThrow(() ->
                new NoSuchCompanyException("No analytical data for " + company + ", due to insufficient data."));
    }

    @Override
    public List<Oscillator> getLatestOscillators(String company) {
        return getLatest(company).oscillators();
    }

    @Override
    public List<MovingAverage> getLatestMAs(String company) {
        return getLatest(company).MAs();
    }

    @Override
    public Map<String, Long> oscillatorSummary(String company) {
        return getLatest(company).oscillatorSummary();
    }

    @Override
    public Map<String, Long> MASummary(String company) {
        return getLatest(company).movingAverageSummary();
    }
}
