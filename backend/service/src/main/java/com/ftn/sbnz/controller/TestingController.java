package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.CropStateDTO;
import com.ftn.sbnz.dto.WeatherInputDTO;
import com.ftn.sbnz.leservice.ClockService;
import com.ftn.sbnz.leservice.CropRuleEvaluationService;
import com.ftn.sbnz.leservice.CropService;
import com.ftn.sbnz.leservice.CultureReferenceService;
import com.ftn.sbnz.leservice.PredefinedWeatherService;
import com.ftn.sbnz.leservice.WeatherModeService;
import com.ftn.sbnz.leservice.WeatherSimulationService;
import com.ftn.sbnz.model.Action;
import com.ftn.sbnz.model.ActionName;
import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.model.CultureStatus;
import com.ftn.sbnz.model.Problem;
import com.ftn.sbnz.model.ProblemName;
import com.ftn.sbnz.model.WeatherDayInfo;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
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
    private final CropService cropService;
    private final CultureReferenceService cultureReferenceService;

    public TestingController(ClockService clockService,
                              WeatherSimulationService weatherSimulationService,
                              WeatherModeService weatherModeService,
                              PredefinedWeatherService predefinedWeatherService,
                              CropRuleEvaluationService cropRuleEvaluationService,
                              CropService cropService,
                              CultureReferenceService cultureReferenceService) {
        this.clockService = clockService;
        this.weatherSimulationService = weatherSimulationService;
        this.weatherModeService = weatherModeService;
        this.predefinedWeatherService = predefinedWeatherService;
        this.cropRuleEvaluationService = cropRuleEvaluationService;
        this.cropService = cropService;
        this.cultureReferenceService = cultureReferenceService;
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
    public ResponseEntity<?> wipeAndStartAt(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
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

    @PostMapping("/plant-crop")
    public ResponseEntity<?> plantCrop(@RequestParam CultureName culture) {
        Month plantingMonth = cultureReferenceService.getPlantingMonth(culture);
        if (plantingMonth == null || clockService.getCurrentDate().getMonth() != plantingMonth) {
            return ResponseEntity.badRequest().body(
                    "Kultura " + culture + " se sadi tokom meseca " + plantingMonth + ", trenutni mesec ne odgovara.");
        }

        Crop crop = new Crop();
        crop.setCultureName(culture);
        crop.setStatus(CultureStatus.OK);
        crop.setLevel(1);
        crop.setSize(10);
        crop.setNumber(5);
        Crop saved = cropService.save(crop);
        return ResponseEntity.ok(saved.getId());
    }

    @PostMapping("/log-action")
    public ResponseEntity<?> logAction(@RequestParam Long cropId,
                                        @RequestParam ActionName action,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Crop crop = cropService.findById(cropId).orElseThrow();
        Action newAction = new Action();
        newAction.setName(action);
        newAction.setDone(date);
        crop.getActions().add(newAction);
        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/report-problem")
    public ResponseEntity<?> reportProblem(@RequestParam Long cropId, @RequestParam ProblemName problem) {
        Crop crop = cropService.findById(cropId).orElseThrow();
        Problem newProblem = new Problem();
        newProblem.setName(problem);
        newProblem.setAppeared(clockService.getCurrentDate());
        crop.getProblems().add(newProblem);
        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/crop/{id}")
    public ResponseEntity<?> getCrop(@PathVariable Long id) {
        Crop crop = cropService.findById(id).orElseThrow();
        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
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
}