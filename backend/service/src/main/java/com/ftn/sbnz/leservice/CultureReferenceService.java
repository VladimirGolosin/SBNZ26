package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.model.CultureReference;
import com.ftn.sbnz.repo.CultureReferenceRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CultureReferenceService {

    private final CultureReferenceRepository repository;
    private final Map<CultureName, CultureReference> cache = new EnumMap<>(CultureName.class);

    public CultureReferenceService(CultureReferenceRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void load() {
        cache.clear();
        for (CultureReference ref : repository.findAll()) {
            cache.put(ref.getCultureName(), ref);
        }
    }

    public double getOptimalTemperature(CultureName cultureName) {
        return cache.get(cultureName).getOptimalTemperature();
    }

    public double getOptimalWeeklyRainfall(CultureName cultureName) {
        return cache.get(cultureName).getOptimalWeeklyRainfall();
    }

    public Month getPlantingMonth(CultureName cultureName) {
        return cache.get(cultureName).getPlantingMonth();
    }

    public Month getHarvestMonth(CultureName cultureName) {
        return cache.get(cultureName).getHarvestMonth();
    }

    public List<CultureName> getRecommendedCultures(Month currentMonth) {
        return cache.values().stream()
                .filter(ref -> ref.getPlantingMonth() == currentMonth)
                .map(CultureReference::getCultureName)
                .collect(Collectors.toList());
    }

    public void reload() {
        load();
    }
}