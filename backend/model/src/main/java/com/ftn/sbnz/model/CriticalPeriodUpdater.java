package com.ftn.sbnz.model;

import java.time.LocalDate;

public interface CriticalPeriodUpdater {
    void updateCulture(CultureName culture, boolean criticalToday, LocalDate currentDate);
}