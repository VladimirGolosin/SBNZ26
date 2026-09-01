package com.ftn.sbnz.leservice;

import com.ftn.sbnz.dto.CropStateDTO;
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

    public CropRuleEvaluationService(KieContainer kieContainer, CropService cropService) {
        this.kieContainer = kieContainer;
        this.cropService = cropService;
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

        CropStateDTO dto = new CropStateDTO();
        dto.setId(crop.getId());
        dto.setCultureName(crop.getCultureName());
        dto.setLevel(crop.getLevel());
        dto.setStatus(crop.getStatus());
        dto.setRecommendations(recommendations);
        return dto;
    }
}