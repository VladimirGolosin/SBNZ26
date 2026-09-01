package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.WeatherInputDTO;
import com.ftn.sbnz.leservice.ClockService;
import com.ftn.sbnz.leservice.PredefinedWeatherService;
import com.ftn.sbnz.leservice.WeatherModeService;
import com.ftn.sbnz.leservice.WeatherSimulationService;
import com.ftn.sbnz.model.WeatherDayInfo;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clock")
@CrossOrigin(origins = "http://localhost:4200")
public class ClockController {

    private final ClockService clockService;
    private final WeatherSimulationService weatherSimulationService;
    private final WeatherModeService weatherModeService;
    private final PredefinedWeatherService predefinedWeatherService;

    public ClockController(ClockService clockService,
                            WeatherSimulationService weatherSimulationService,
                            WeatherModeService weatherModeService,
                            PredefinedWeatherService predefinedWeatherService) {
        this.clockService = clockService;
        this.weatherSimulationService = weatherSimulationService;
        this.weatherModeService = weatherModeService;
        this.predefinedWeatherService = predefinedWeatherService;
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

    @PostMapping("/advance-days")
    public ResponseEntity<?> advanceDays(@RequestParam int days) {
        clockService.advanceDays(days);
        return ResponseEntity.ok(clockService.getCurrentDate());
    }

    @PostMapping("/advance-to-date")
    public ResponseEntity<?> advanceToDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        clockService.advanceToDate(date);
        return ResponseEntity.ok(clockService.getCurrentDate());
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

    @PostMapping("/wipe-and-restart")
    public ResponseEntity<?> wipeAndRestart() {
        clockService.wipeAndRestart();
        return ResponseEntity.ok("Wiped and restarted at today's date");
    }

    @PostMapping("/wipe-and-start-at")
    public ResponseEntity<?> wipeAndStartAt(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        clockService.wipeAndStartAt(date);
        return ResponseEntity.ok("Wiped and started at " + date);
    }
}