package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.CriticalPeriodRange;
import com.ftn.sbnz.model.CriticalPeriodStatus;
import com.ftn.sbnz.model.CriticalPeriodUpdater;
import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.repo.CriticalPeriodStatusRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class CriticalPeriodTrackerService implements CriticalPeriodUpdater {

    private static class Status {
        boolean critical;
        LocalDate since;
    }

    private final CriticalPeriodService criticalPeriodService;
    private final CriticalPeriodStatusRepository statusRepository;
    private final KieContainer kieContainer;
    private final CropService cropService;
    private final Map<CultureName, Status> statusMap = new EnumMap<>(CultureName.class);

    public CriticalPeriodTrackerService(CriticalPeriodService criticalPeriodService,
                                         CriticalPeriodStatusRepository statusRepository,
                                         KieContainer kieContainer,
                                         CropService cropService) {
        this.criticalPeriodService = criticalPeriodService;
        this.statusRepository = statusRepository;
        this.kieContainer = kieContainer;
        this.cropService = cropService;
    }

    public void loadFromDb() {
        statusMap.clear();
        for (CriticalPeriodStatus entity : statusRepository.findAll()) {
            Status status = new Status();
            status.critical = entity.isCritical();
            status.since = entity.getSince();
            statusMap.put(entity.getCultureName(), status);
        }
    }

    @Override
    @Transactional
    public void updateCulture(CultureName culture, boolean criticalToday, LocalDate currentDate) {
        Status status = statusMap.computeIfAbsent(culture, c -> new Status());

        if (criticalToday) {
            if (!status.critical) {
                status.critical = true;
                status.since = currentDate;
            }
        } else {
            if (status.critical) {
                LocalDate dateFrom = status.since;
                LocalDate dateTo = currentDate.minusDays(1);
                criticalPeriodService.create(culture, dateFrom, dateTo);
                checkIrrigationCompliance(culture, dateFrom, dateTo);
                status.critical = false;
                status.since = null;
            }
        }

        persist(culture, status);
    }

    private void checkIrrigationCompliance(CultureName culture, LocalDate dateFrom, LocalDate dateTo) {
        List<Crop> crops = cropService.findActiveCropsByCulture(culture);
        for (Crop crop : crops) {
            KieSession kSession = kieContainer.newKieSession("gardenForwardKsession");
            kSession.setGlobal("currentDate", dateTo);
            kSession.insert(new CriticalPeriodRange(dateFrom, dateTo));
            kSession.insert(crop);
            for (var action : crop.getActions()) {
                kSession.insert(action);
            }
            for (var problem : crop.getProblems()) {
                kSession.insert(problem);
            }
            kSession.fireAllRules();
            kSession.dispose();
            cropService.save(crop);
        }
    }

    private void persist(CultureName culture, Status status) {
        CriticalPeriodStatus entity = statusRepository.findById(culture).orElse(new CriticalPeriodStatus());
        entity.setCultureName(culture);
        entity.setCritical(status.critical);
        entity.setSince(status.since);
        statusRepository.save(entity);
    }

    public boolean isCritical(CultureName culture) {
        Status status = statusMap.get(culture);
        return status != null && status.critical;
    }

    public LocalDate getCriticalSince(CultureName culture) {
        Status status = statusMap.get(culture);
        return status != null ? status.since : null;
    }

    public void reset() {
        statusMap.clear();
        statusRepository.deleteAll();
    }
}