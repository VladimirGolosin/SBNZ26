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
    private final WeatherSimulationService weatherSimulationService;
    private final CriticalPeriodTrackerService criticalPeriodTrackerService;

    private KieSession kSession;
    private SessionPseudoClock clock;
    private LocalDate currentDate;
    private WeatherDayInfo lastReading;

    public ClockService(KieContainer kieContainer,
                         WeatherDayInfoService weatherDayInfoService,
                         WeatherSimulationService weatherSimulationService,
                         CriticalPeriodTrackerService criticalPeriodTrackerService) {
        this.kieContainer = kieContainer;
        this.weatherDayInfoService = weatherDayInfoService;
        this.weatherSimulationService = weatherSimulationService;
        this.criticalPeriodTrackerService = criticalPeriodTrackerService;
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
        criticalPeriodTrackerService.reset();
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

    public WeatherDayInfo advanceOneDayAuto() {
        double[] values = weatherSimulationService.generateNextReading(currentDate.plusDays(1));
        return advanceOneDay(values[0], values[1]);
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public WeatherDayInfo getLastReading() {
        return lastReading;
    }
}