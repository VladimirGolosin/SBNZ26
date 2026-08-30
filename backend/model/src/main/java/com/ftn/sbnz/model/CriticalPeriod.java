package com.ftn.sbnz.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "critical_periods")
public class CriticalPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CultureName cultureName;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CultureName getCultureName() {
        return cultureName;
    }

    public void setCultureName(CultureName cultureName) {
        this.cultureName = cultureName;
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