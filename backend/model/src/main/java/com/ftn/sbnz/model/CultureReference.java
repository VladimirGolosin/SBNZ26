package com.ftn.sbnz.model;

import javax.persistence.*;
import java.time.Month;

@Entity
@Table(name = "culture_reference")
public class CultureReference {

    @Id
    @Enumerated(EnumType.STRING)
    private CultureName cultureName;

    private double optimalTemperature;

    private double optimalWeeklyRainfall;

    @Enumerated(EnumType.STRING)
    private Month plantingMonth;

    @Enumerated(EnumType.STRING)
    private Month harvestMonth;

    public CultureName getCultureName() {
        return cultureName;
    }

    public void setCultureName(CultureName cultureName) {
        this.cultureName = cultureName;
    }

    public double getOptimalTemperature() {
        return optimalTemperature;
    }

    public void setOptimalTemperature(double optimalTemperature) {
        this.optimalTemperature = optimalTemperature;
    }

    public double getOptimalWeeklyRainfall() {
        return optimalWeeklyRainfall;
    }

    public void setOptimalWeeklyRainfall(double optimalWeeklyRainfall) {
        this.optimalWeeklyRainfall = optimalWeeklyRainfall;
    }

    public Month getPlantingMonth() {
        return plantingMonth;
    }

    public void setPlantingMonth(Month plantingMonth) {
        this.plantingMonth = plantingMonth;
    }

    public Month getHarvestMonth() {
        return harvestMonth;
    }

    public void setHarvestMonth(Month harvestMonth) {
        this.harvestMonth = harvestMonth;
    }
}