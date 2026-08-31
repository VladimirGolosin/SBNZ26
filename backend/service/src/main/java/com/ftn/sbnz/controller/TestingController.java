package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.WeatherInputDTO;
import com.ftn.sbnz.leservice.ClockService;
import com.ftn.sbnz.leservice.PredefinedWeatherService;
import com.ftn.sbnz.leservice.WeatherModeService;
import com.ftn.sbnz.leservice.WeatherSimulationService;
import com.ftn.sbnz.model.WeatherDayInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public TestingController(ClockService clockService,
                              WeatherSimulationService weatherSimulationService,
                              WeatherModeService weatherModeService,
                              PredefinedWeatherService predefinedWeatherService) {
        this.clockService = clockService;
        this.weatherSimulationService = weatherSimulationService;
        this.weatherModeService = weatherModeService;
        this.predefinedWeatherService = predefinedWeatherService;
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

    @PostMapping("/reset")
    public ResponseEntity<?> reset() {
        clockService.reset();
        return ResponseEntity.ok("Clock reset");
    }
}