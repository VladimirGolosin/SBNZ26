package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.WeatherDayInfo;
import org.drools.core.time.SessionPseudoClock;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
public class ClockService {

    private final KieContainer kieContainer;
    private final WeatherDayInfoService weatherDayInfoService;

    private KieSession kSession;
    private SessionPseudoClock clock;
    private LocalDate currentDate;
    private WeatherDayInfo lastReading;

    public ClockService(KieContainer kieContainer, WeatherDayInfoService weatherDayInfoService) {
        this.kieContainer = kieContainer;
        this.weatherDayInfoService = weatherDayInfoService;
    }

    @PostConstruct
    public void init() {
        reset();
    }

    public void reset() {
        if (kSession != null) {
            kSession.dispose();
        }
        kSession = kieContainer.newKieSession("gardenCepKsession");
        clock = kSession.getSessionClock();
        currentDate = LocalDate.now();
        lastReading = null;
    }

    public WeatherDayInfo advanceOneDay(double temperature, double rainfall) {
        clock.advanceTime(1, TimeUnit.DAYS);
        currentDate = currentDate.plusDays(1);

        WeatherDayInfo reading = weatherDayInfoService.create(temperature, rainfall, currentDate);

        kSession.insert(reading);
        kSession.fireAllRules();

        lastReading = reading;
        return reading;
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public WeatherDayInfo getLastReading() {
        return lastReading;
    }
}