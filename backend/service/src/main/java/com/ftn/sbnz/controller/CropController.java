package com.ftn.sbnz.controller;

import com.ftn.sbnz.dto.CropStateDTO;
import com.ftn.sbnz.leservice.ClockService;
import com.ftn.sbnz.leservice.CropRuleEvaluationService;
import com.ftn.sbnz.leservice.CropService;
import com.ftn.sbnz.leservice.CultureReferenceService;
import com.ftn.sbnz.model.Action;
import com.ftn.sbnz.model.ActionName;
import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.model.CultureStatus;
import com.ftn.sbnz.model.Problem;
import com.ftn.sbnz.model.ProblemName;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "http://localhost:4200")
public class CropController {

    private final ClockService clockService;
    private final CropService cropService;
    private final CropRuleEvaluationService cropRuleEvaluationService;
    private final CultureReferenceService cultureReferenceService;

    public CropController(ClockService clockService,
                           CropService cropService,
                           CropRuleEvaluationService cropRuleEvaluationService,
                           CultureReferenceService cultureReferenceService) {
        this.clockService = clockService;
        this.cropService = cropService;
        this.cropRuleEvaluationService = cropRuleEvaluationService;
        this.cultureReferenceService = cultureReferenceService;
    }

    @PostMapping("/plant")
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getCrop(@PathVariable Long id) {
        Crop crop = findCropOr404(id);
        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/actions")
    public ResponseEntity<?> logAction(@PathVariable Long id,
                                        @RequestParam ActionName action,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Crop crop = findCropOr404(id);
        Action newAction = new Action();
        newAction.setName(action);
        newAction.setDone(date);
        crop.getActions().add(newAction);
        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/problems")
    public ResponseEntity<?> reportProblem(@PathVariable Long id, @RequestParam ProblemName problem) {
        Crop crop = findCropOr404(id);
        Problem newProblem = new Problem();
        newProblem.setName(problem);
        newProblem.setAppeared(clockService.getCurrentDate());
        crop.getProblems().add(newProblem);
        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/problems/{problemId}/resolving")
    public ResponseEntity<?> markProblemResolving(@PathVariable Long id, @PathVariable Long problemId) {
        Crop crop = findCropOr404(id);
        findProblemOr404(crop, problemId).setAddressed(clockService.getCurrentDate());
        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/problems/{problemId}/resolved")
    public ResponseEntity<?> markProblemResolved(@PathVariable Long id, @PathVariable Long problemId) {
        Crop crop = findCropOr404(id);
        findProblemOr404(crop, problemId).setFinalized(clockService.getCurrentDate());
        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/collect")
    public ResponseEntity<?> collectCrop(@PathVariable Long id) {
        try {
            Crop crop = cropService.collectCrop(id, clockService.getCurrentDate());
            CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Crop findCropOr404(Long id) {
        return cropService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Crop not found: " + id));
    }

    private Problem findProblemOr404(Crop crop, Long problemId) {
        return crop.getProblems().stream()
                .filter(p -> p.getId().equals(problemId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Problem not found: " + problemId));
    }
}