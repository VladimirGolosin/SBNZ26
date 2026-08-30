package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.WeatherDayInfo;
import com.ftn.sbnz.repo.WeatherDayInfoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
}