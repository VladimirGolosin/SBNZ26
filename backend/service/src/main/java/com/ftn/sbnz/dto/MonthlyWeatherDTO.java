package com.ftn.sbnz.dto;

public class MonthlyWeatherDTO {

    private String month;
    private double avgTemperature;
    private double avgRainfall;

    public MonthlyWeatherDTO() {
    }

    public MonthlyWeatherDTO(String month, double avgTemperature, double avgRainfall) {
        this.month = month;
        this.avgTemperature = avgTemperature;
        this.avgRainfall = avgRainfall;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getAvgTemperature() {
        return avgTemperature;
    }

    public void setAvgTemperature(double avgTemperature) {
        this.avgTemperature = avgTemperature;
    }

    public double getAvgRainfall() {
        return avgRainfall;
    }

    public void setAvgRainfall(double avgRainfall) {
        this.avgRainfall = avgRainfall;
    }
}