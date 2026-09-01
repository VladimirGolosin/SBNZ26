package com.ftn.sbnz.model;

public interface CultureThresholdProvider {
    double getOptimalTemperature(CultureName cultureName);
    double getOptimalWeeklyRainfall(CultureName cultureName);
}