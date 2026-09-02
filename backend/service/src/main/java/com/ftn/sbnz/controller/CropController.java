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
import com.ftn.sbnz.model.User;
import com.ftn.sbnz.repo.UserRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "http://localhost:4200")
public class CropController {

    private final ClockService clockService;
    private final CropService cropService;
    private final CropRuleEvaluationService cropRuleEvaluationService;
    private final CultureReferenceService cultureReferenceService;
    private final UserRepository userRepository;

    public CropController(ClockService clockService,
                           CropService cropService,
                           CropRuleEvaluationService cropRuleEvaluationService,
                           CultureReferenceService cultureReferenceService,
                           UserRepository userRepository) {
        this.clockService = clockService;
        this.cropService = cropService;
        this.cropRuleEvaluationService = cropRuleEvaluationService;
        this.cultureReferenceService = cultureReferenceService;
        this.userRepository = userRepository;
    }

    @PostMapping("/plant")
    public ResponseEntity<?> plantCrop(@RequestParam CultureName culture,
                                        @RequestParam Long userId,
                                        @RequestParam int size,
                                        @RequestParam int number) {
        Month plantingMonth = cultureReferenceService.getPlantingMonth(culture);
        if (plantingMonth == null || clockService.getCurrentDate().getMonth() != plantingMonth) {
            return ResponseEntity.badRequest().body(
                    "The culture " + culture + " is planted during " + plantingMonth + "; the current month does not match.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId));

        Crop crop = new Crop();
        crop.setCultureName(culture);
        crop.setStatus(CultureStatus.OK);
        crop.setLevel(1);
        crop.setSize(size);
        crop.setNumber(number);
        crop.setUser(user);
        crop.setPlantedDate(clockService.getCurrentDate());
        Crop saved = cropService.save(crop);
        return ResponseEntity.ok(saved.getId());
    }

    @GetMapping
    public ResponseEntity<?> listCrops(@RequestParam Long userId, @RequestParam(defaultValue = "true") boolean active) {
        List<Crop> crops = cropService.findCropsForUser(userId, active);
        List<CropStateDTO> result = crops.stream()
                .map(c -> cropRuleEvaluationService.evaluateCropRules(c, clockService.getCurrentDate()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
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

        boolean alreadyLogged = crop.getActions().stream()
                .anyMatch(a -> a.getName() == action && a.getDone() != null && a.getDone().equals(date));
        if (alreadyLogged) {
            return ResponseEntity.badRequest().body("This action was already logged for this date.");
        }

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

        boolean alreadyActive = crop.getProblems().stream()
                .anyMatch(p -> p.getName() == problem && p.getAddressed() == null);
        if (alreadyActive) {
            return ResponseEntity.badRequest().body("This problem is already reported and unresolved for this crop.");
        }

        Problem newProblem = new Problem();
        newProblem.setName(problem);
        newProblem.setAppeared(clockService.getCurrentDate());
        crop.getProblems().add(newProblem);
        CropStateDTO result = cropRuleEvaluationService.evaluateCropRules(crop, clockService.getCurrentDate());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/problems/resolve")
    public ResponseEntity<?> resolveProblem(@PathVariable Long id, @RequestParam ProblemName problemName) {
        Crop crop = findCropOr404(id);
        Problem problem = crop.getProblems().stream()
                .filter(p -> p.getName() == problemName && p.getAddressed() == null)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active problem of that type on this crop."));
        problem.setAddressed(clockService.getCurrentDate());
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