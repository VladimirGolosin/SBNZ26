package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.CultureName;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

@Service
public class CriticalPeriodTrackerService {

    private static class Status {
        boolean critical;
        LocalDate since;
    }

    private final CriticalPeriodService criticalPeriodService;
    private final Map<CultureName, Status> statusMap = new EnumMap<>(CultureName.class);

    public CriticalPeriodTrackerService(CriticalPeriodService criticalPeriodService) {
        this.criticalPeriodService = criticalPeriodService;
    }

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
    }
}