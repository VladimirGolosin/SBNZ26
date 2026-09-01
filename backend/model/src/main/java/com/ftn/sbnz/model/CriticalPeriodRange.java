package com.ftn.sbnz.model;

import java.time.LocalDate;

public class CriticalPeriodRange {

    private LocalDate dateFrom;
    private LocalDate dateTo;

    public CriticalPeriodRange() {
    }

    public CriticalPeriodRange(LocalDate dateFrom, LocalDate dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}