package com.ftn.sbnz.leservice;

import com.ftn.sbnz.dto.CropStateDTO;
import com.ftn.sbnz.model.ActionName;
import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.Recommendation;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CropRuleEvaluationService {

    private final KieContainer kieContainer;
    private final CropService cropService;
    private final CriticalPeriodTrackerService criticalPeriodTrackerService;

    public CropRuleEvaluationService(KieContainer kieContainer,
                                      CropService cropService,
                                      CriticalPeriodTrackerService criticalPeriodTrackerService) {
        this.kieContainer = kieContainer;
        this.cropService = cropService;
        this.criticalPeriodTrackerService = criticalPeriodTrackerService;
    }

    public CropStateDTO evaluateCropRules(Crop crop, LocalDate currentDate) {
        KieSession kSession = kieContainer.newKieSession("gardenForwardKsession");

        kSession.setGlobal("currentDate", currentDate);

        kSession.insert(crop);
        for (var action : crop.getActions()) {
            kSession.insert(action);
        }
        for (var problem : crop.getProblems()) {
            kSession.insert(problem);
        }

        kSession.fireAllRules();

        List<Recommendation> recommendations = kSession.getObjects(o -> o instanceof Recommendation)
                .stream()
                .map(o -> (Recommendation) o)
                .collect(Collectors.toList());

        kSession.dispose();

        cropService.save(crop);

        boolean irrigatedToday = crop.getActions().stream()
                .anyMatch(a -> a.getName() == ActionName.IRRIGATION
                        && a.getDone() != null
                        && a.getDone().equals(currentDate));

        if (crop.isActive() && criticalPeriodTrackerService.isCritical(crop.getCultureName()) && !irrigatedToday) {
            recommendations.add(new Recommendation(
                    Recommendation.Type.NEEDS_IRRIGATION,
                    "Critical period: irrigation is recommended.",
                    ActionName.IRRIGATION
            ));
        }

        CropStateDTO dto = new CropStateDTO();
        dto.setId(crop.getId());
        dto.setCultureName(crop.getCultureName());
        dto.setLevel(crop.getLevel());
        dto.setStatus(crop.getStatus());
        dto.setSize(crop.getSize());
        dto.setPlantedDate(crop.getPlantedDate());
        dto.setRecommendations(recommendations);
        return dto;
    }
}