package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.PredefinedWeather;
import com.ftn.sbnz.repo.PredefinedWeatherRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class PredefinedWeatherService {

    private final PredefinedWeatherRepository repository;
    private final Map<Integer, PredefinedWeather> cache = new HashMap<>();

    public PredefinedWeatherService(PredefinedWeatherRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void load() {
        cache.clear();
        for (PredefinedWeather entry : repository.findAll()) {
            cache.put(entry.getDayOfYear(), entry);
        }
    }

    public double[] getReading(int dayOfYear) {
        PredefinedWeather entry = cache.get(dayOfYear);
        if (entry == null && dayOfYear == 366) {
            entry = cache.get(365);
        }
        if (entry == null) {
            throw new IllegalStateException("No predefined weather for day of year " + dayOfYear);
        }
        return new double[] { entry.getTemperature(), entry.getRainfall() };
    }

    public void reload() {
        load();
    }
}