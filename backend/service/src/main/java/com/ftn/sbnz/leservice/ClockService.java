package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureStatus;
import com.ftn.sbnz.model.WeatherDayInfo;
import org.drools.core.time.SessionPseudoClock;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ClockService {

    private final KieContainer kieContainer;
    private final WeatherDayInfoService weatherDayInfoService;
    private final WeatherSimulationService weatherSimulationService;
    private final CriticalPeriodTrackerService criticalPeriodTrackerService;
    private final CriticalPeriodService criticalPeriodService;
    private final PredefinedWeatherService predefinedWeatherService;
    private final WeatherModeService weatherModeService;
    private final SimulationStateService simulationStateService;
    private final CropService cropService;
    private final CropRuleEvaluationService cropRuleEvaluationService;
    private final CultureReferenceService cultureReferenceService;

    private KieSession kSession;
    private SessionPseudoClock clock;
    private LocalDate currentDate;
    private WeatherDayInfo lastReading;

    public ClockService(KieContainer kieContainer,
                         WeatherDayInfoService weatherDayInfoService,
                         WeatherSimulationService weatherSimulationService,
                         CriticalPeriodTrackerService criticalPeriodTrackerService,
                         CriticalPeriodService criticalPeriodService,
                         PredefinedWeatherService predefinedWeatherService,
                         WeatherModeService weatherModeService,
                         SimulationStateService simulationStateService,
                         CropService cropService,
                         CropRuleEvaluationService cropRuleEvaluationService,
                         CultureReferenceService cultureReferenceService) {
        this.kieContainer = kieContainer;
        this.weatherDayInfoService = weatherDayInfoService;
        this.weatherSimulationService = weatherSimulationService;
        this.criticalPeriodTrackerService = criticalPeriodTrackerService;
        this.criticalPeriodService = criticalPeriodService;
        this.predefinedWeatherService = predefinedWeatherService;
        this.weatherModeService = weatherModeService;
        this.simulationStateService = simulationStateService;
        this.cropService = cropService;
        this.cropRuleEvaluationService = cropRuleEvaluationService;
        this.cultureReferenceService = cultureReferenceService;
    }

    @PostConstruct
    public void init() {
        openFreshSession();
        criticalPeriodTrackerService.loadFromDb();

        Optional<LocalDate> savedDate = simulationStateService.getCurrentDate();
        if (savedDate.isPresent()) {
            currentDate = savedDate.get();
            backfillLastSevenDays();
        } else {
            currentDate = LocalDate.now();
            simulationStateService.saveCurrentDate(currentDate);
        }
    }

    private void openFreshSession() {
        if (kSession != null) {
            kSession.dispose();
        }
        kSession = kieContainer.newKieSession("gardenCepKsession");
        clock = kSession.getSessionClock();
        kSession.setGlobal("criticalPeriodUpdater", criticalPeriodTrackerService);
        kSession.setGlobal("cultureThresholdProvider", cultureReferenceService);
    }

    private void backfillLastSevenDays() {
        List<WeatherDayInfo> lastSeven = weatherDayInfoService.getLastSevenDaysAscending();
        for (WeatherDayInfo reading : lastSeven) {
            clock.advanceTime(1, TimeUnit.DAYS);
            kSession.setGlobal("currentDate", reading.getDate());
            kSession.insert(reading);
            kSession.fireAllRules();
        }
        if (!lastSeven.isEmpty()) {
            lastReading = lastSeven.get(lastSeven.size() - 1);
        }
    }

    private void wipeAllSimulationData() {
        weatherDayInfoService.deleteAll();
        criticalPeriodService.deleteAll();
        criticalPeriodTrackerService.reset();
        cropService.deleteAll();
        weatherSimulationService.reset();
        weatherModeService.reset();
    }

    public void wipeAndRestart() {
        openFreshSession();
        currentDate = LocalDate.now();
        lastReading = null;
        wipeAllSimulationData();
        simulationStateService.saveCurrentDate(currentDate);
    }

    public void wipeAndStartAt(LocalDate startDate) {
        openFreshSession();
        currentDate = startDate;
        lastReading = null;
        wipeAllSimulationData();
        simulationStateService.saveCurrentDate(currentDate);
    }

    public WeatherDayInfo advanceOneDay(double temperature, double rainfall) {
        clock.advanceTime(1, TimeUnit.DAYS);
        currentDate = currentDate.plusDays(1);
        kSession.setGlobal("currentDate", currentDate);

        WeatherDayInfo reading = weatherDayInfoService.create(temperature, rainfall, currentDate);

        kSession.insert(reading);
        kSession.fireAllRules();

        checkCropNeglect();

        lastReading = reading;
        simulationStateService.saveCurrentDate(currentDate);
        return reading;
    }

    private void checkCropNeglect() {
        List<Crop> activeCrops = cropService.findActiveCrops();
        for (Crop crop : activeCrops) {
            if (currentDate.getMonth() == Month.DECEMBER && crop.isActive()) {
                crop.setStatus(CultureStatus.ABANDONED);
                cropService.save(crop);
            } else {
                cropRuleEvaluationService.evaluateCropRules(crop, currentDate);
            }
        }
    }

    public WeatherDayInfo advanceOneDayAuto() {
        LocalDate nextDate = currentDate.plusDays(1);

        double[] values;
        if (weatherModeService.getMode() == WeatherModeService.Mode.PREDEFINED) {
            values = predefinedWeatherService.getReading(nextDate.getDayOfYear());
        } else {
            values = weatherSimulationService.generateNextReading(nextDate);
        }

        return advanceOneDay(values[0], values[1]);
    }

    public void advanceDays(int days) {
        for (int i = 0; i < days; i++) {
            advanceOneDayAuto();
        }
    }

    public void advanceToDate(LocalDate targetDate) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(currentDate, targetDate);
        if (days < 0) {
            throw new IllegalArgumentException("Target date is in the past relative to current simulated date");
        }
        advanceDays((int) days);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void onMidnight() {
        advanceOneDayAuto();
    }

    public LocalDate getCurrentDate() {
        return currentDate;
    }

    public WeatherDayInfo getLastReading() {
        return lastReading;
    }
}