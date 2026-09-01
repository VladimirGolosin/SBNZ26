package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.CriticalPeriodStatus;
import com.ftn.sbnz.model.CriticalPeriodUpdater;
import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.repo.CriticalPeriodStatusRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

@Service
public class CriticalPeriodTrackerService implements CriticalPeriodUpdater {

    private static class Status {
        boolean critical;
        LocalDate since;
    }

    private final CriticalPeriodService criticalPeriodService;
    private final CriticalPeriodStatusRepository statusRepository;
    private final Map<CultureName, Status> statusMap = new EnumMap<>(CultureName.class);

    public CriticalPeriodTrackerService(CriticalPeriodService criticalPeriodService,
                                         CriticalPeriodStatusRepository statusRepository) {
        this.criticalPeriodService = criticalPeriodService;
        this.statusRepository = statusRepository;
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
    public void updateCulture(CultureName culture, boolean criticalToday, LocalDate currentDate) {
        Status status = statusMap.computeIfAbsent(culture, c -> new Status());

        if (criticalToday) {
            if (!status.critical) {
                status.critical = true;
                status.since = currentDate;
            }
        } else {
            if (status.critical) {
                criticalPeriodService.create(culture, status.since, currentDate.minusDays(1));
                status.critical = false;
                status.since = null;
            }
        }

        persist(culture, status);
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