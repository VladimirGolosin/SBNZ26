package com.ftn.sbnz.leservice;

import com.ftn.sbnz.dto.MonthlyWeatherDTO;
import com.ftn.sbnz.model.WeatherDayInfo;
import com.ftn.sbnz.repo.WeatherDayInfoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeatherDayInfoService {

    private final WeatherDayInfoRepository repository;

    public WeatherDayInfoService(WeatherDayInfoRepository repository) {
        this.repository = repository;
    }

    public WeatherDayInfo create(double temperature, double rainfall, LocalDate date) {
        WeatherDayInfo reading = new WeatherDayInfo();
        reading.setTemperature(temperature);
        reading.setRainfall(rainfall);
        reading.setDate(date);
        return repository.save(reading);
    }

    public List<WeatherDayInfo> getLastSevenDaysAscending() {
        List<WeatherDayInfo> lastSeven = repository.findTop7ByOrderByDateDesc();
        Collections.reverse(lastSeven);
        return lastSeven;
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public List<Integer> getAvailableYears() {
        return repository.findAll().stream()
                .map(w -> w.getDate().getYear())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<MonthlyWeatherDTO> getMonthlyAggregates(int year) {
        List<WeatherDayInfo> yearData = repository.findAll().stream()
                .filter(w -> w.getDate().getYear() == year)
                .collect(Collectors.toList());

        Map<Month, List<WeatherDayInfo>> byMonth = yearData.stream()
                .collect(Collectors.groupingBy(w -> w.getDate().getMonth()));

        List<MonthlyWeatherDTO> result = new ArrayList<>();
        for (Month m : Month.values()) {
            List<WeatherDayInfo> monthData = byMonth.getOrDefault(m, List.of());
            double avgTemp = monthData.stream().mapToDouble(WeatherDayInfo::getTemperature).average().orElse(0);
            double avgRain = monthData.stream().mapToDouble(WeatherDayInfo::getRainfall).average().orElse(0);
            result.add(new MonthlyWeatherDTO(m.toString(), avgTemp, avgRain));
        }
        return result;
    }
}