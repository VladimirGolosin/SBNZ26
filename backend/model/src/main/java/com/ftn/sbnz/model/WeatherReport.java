package com.ftn.sbnz.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "weather_reports")
public class WeatherReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "weather_report_id")
    private List<WeatherDayInfo> data = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "weather_report_id")
    private List<CriticalPeriod> criticalDays = new ArrayList<>();

    private Date dateFrom;

    private Date dateTo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<WeatherDayInfo> getData() {
        return data;
    }

    public void setData(List<WeatherDayInfo> data) {
        this.data = data;
    }

    public List<CriticalPeriod> getCriticalDays() {
        return criticalDays;
    }

    public void setCriticalDays(List<CriticalPeriod> criticalDays) {
        this.criticalDays = criticalDays;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
