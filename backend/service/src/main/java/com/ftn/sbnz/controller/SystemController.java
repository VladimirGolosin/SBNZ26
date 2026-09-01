package com.ftn.sbnz.controller;

import com.ftn.sbnz.leservice.ClockService;
import com.ftn.sbnz.leservice.CultureReferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "http://localhost:4200")
public class SystemController {

    private final ClockService clockService;
    private final CultureReferenceService cultureReferenceService;

    public SystemController(ClockService clockService, CultureReferenceService cultureReferenceService) {
        this.clockService = clockService;
        this.cultureReferenceService = cultureReferenceService;
    }

    @GetMapping("/recommended-cultures")
    public ResponseEntity<?> recommendedCultures() {
        return ResponseEntity.ok(cultureReferenceService.getRecommendedCultures(clockService.getCurrentDate().getMonth()));
    }
}