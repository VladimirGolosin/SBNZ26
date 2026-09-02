package com.ftn.sbnz.controller;

import com.ftn.sbnz.leservice.ReportService;
import com.ftn.sbnz.leservice.WeatherDayInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {

    private final WeatherDayInfoService weatherDayInfoService;
    private final ReportService reportService;

    public ReportController(WeatherDayInfoService weatherDayInfoService, ReportService reportService) {
        this.weatherDayInfoService = weatherDayInfoService;
        this.reportService = reportService;
    }

    @GetMapping("/weather/years")
    public ResponseEntity<?> getAvailableYears() {
        return ResponseEntity.ok(weatherDayInfoService.getAvailableYears());
    }

    @GetMapping("/weather")
    public ResponseEntity<?> getWeatherReport(@RequestParam int year) {
        return ResponseEntity.ok(weatherDayInfoService.getMonthlyAggregates(year));
    }

    @GetMapping("/cost-profit")
    public ResponseEntity<?> getCostProfitReport(@RequestParam Long userId) {
        return ResponseEntity.ok(reportService.generateCostProfitReport(userId));
    }
}