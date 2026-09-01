package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.CropStateDTO;
import com.ftn.sbnz.dto.WeatherInputDTO;
import com.ftn.sbnz.leservice.ClockService;
import com.ftn.sbnz.leservice.CropRuleEvaluationService;
import com.ftn.sbnz.leservice.PredefinedWeatherService;
import com.ftn.sbnz.leservice.WeatherModeService;
import com.ftn.sbnz.leservice.WeatherSimulationService;
import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.model.CultureStatus;
import com.ftn.sbnz.model.WeatherDayInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/testing")
@CrossOrigin(origins = "http://localhost:4200")
public class TestingController {

    private final ClockService clockService;
    private final WeatherSimulationService weatherSimulationService;
    private final WeatherModeService weatherModeService;
    private final PredefinedWeatherService predefinedWeatherService;
    private final CropRuleEvaluationService cropRuleEvaluationService;

    public TestingController(ClockService clockService,
                              WeatherSimulationService weatherSimulationService,
                              WeatherModeService weatherModeService,
                              PredefinedWeatherService predefinedWeatherService,
                              CropRuleEvaluationService cropRuleEvaluationService) {
        this.clockService = clockService;
        this.weatherSimulationService = weatherSimulationService;
        this.weatherModeService = weatherModeService;
        this.predefinedWeatherService = predefinedWeatherService;
        this.cropRuleEvaluationService = cropRuleEvaluationService;
    }

    @PostMapping("/advance-day")
    public ResponseEntity<?> advanceDay(@RequestBody WeatherInputDTO input) {
        WeatherDayInfo reading = clockService.advanceOneDay(input.getTemperature(), input.getRainfall());
        return ResponseEntity.ok(reading);
    }

    @PostMapping("/advance-day-auto")
    public ResponseEntity<?> advanceDayAuto() {
        WeatherDayInfo reading = clockService.advanceOneDayAuto();
        return ResponseEntity.ok(reading);
    }

    @PostMapping("/set-profile")
    public ResponseEntity<?> setProfile(@RequestParam WeatherSimulationService.Profile profile) {
        weatherSimulationService.setActiveProfile(profile);
        return ResponseEntity.ok("Profile set to " + profile);
    }

    @PostMapping("/set-weather-mode")
    public ResponseEntity<?> setWeatherMode(@RequestParam WeatherModeService.Mode mode) {
        weatherModeService.setMode(mode);
        return ResponseEntity.ok("Weather mode set to " + mode);
    }

    @PostMapping("/reload-predefined-weather")
    public ResponseEntity<?> reloadPredefinedWeather() {
        predefinedWeatherService.reload();
        return ResponseEntity.ok("Predefined weather reloaded");
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        Map<String, Object> result = new HashMap<>();
        result.put("currentDate", clockService.getCurrentDate());
        result.put("lastReading", clockService.getLastReading());
        result.put("activeProfile", weatherSimulationService.getActiveProfile());
        result.put("weatherMode", weatherModeService.getMode());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/wipe-and-restart")
    public ResponseEntity<?> wipeAndRestart() {
        clockService.wipeAndRestart();
        return ResponseEntity.ok("Wiped and restarted at today's date");
    }

    @PostMapping("/wipe-and-start-at")
    public ResponseEntity<?> wipeAndStartAt(@RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate date) {
        clockService.wipeAndStartAt(date);
        return ResponseEntity.ok("Wiped and started at " + date);
    }

    @PostMapping("/evaluate-test-crop")
    public ResponseEntity<?> evaluateTestCrop() {
        Crop crop = new Crop();
        crop.setCultureName(CultureName.ONION);
        crop.setStatus(CultureStatus.OK);
        crop.setLevel(1);
        crop.setSize(10);
        crop.setNumber(5);

        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
    }
}