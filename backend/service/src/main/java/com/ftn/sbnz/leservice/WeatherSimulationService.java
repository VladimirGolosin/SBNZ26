package com.ftn.sbnz.leservice;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;

@Service
public class WeatherSimulationService {

    private static final double OPTIMAL_TEMPERATURE = 22.0;
    private static final double OPTIMAL_WEEKLY_RAINFALL = 23.3;
    private static final double DAILY_RAINFALL_BASE = OPTIMAL_WEEKLY_RAINFALL / 7.0;

    private final Random random = new Random();

    private double seasonalTemperatureOffset(Month month) {
        switch (month) {
            case DECEMBER: case JANUARY: case FEBRUARY: return -14.0;
            case MARCH: case APRIL: case MAY: return -2.0;
            case JUNE: case JULY: case AUGUST: return 4.0;
            default: return -3.0; // SEP, OCT, NOV
        }
    }

    private double seasonalRainfallMultiplier(Month month) {
        switch (month) {
            case DECEMBER: case JANUARY: case FEBRUARY: return 0.9;
            case MARCH: case APRIL: case MAY: return 1.3;
            case JUNE: case JULY: case AUGUST: return 0.8;
            default: return 1.1; // SEP, OCT, NOV
        }
    }

    public double[] generateNextReading(LocalDate date) {
        Month month = date.getMonth();

        double temperature = OPTIMAL_TEMPERATURE + seasonalTemperatureOffset(month)
                + (random.nextDouble() * 2 - 1) * 2.0;

        double rainfall = Math.max(0, DAILY_RAINFALL_BASE * seasonalRainfallMultiplier(month)
                + (random.nextDouble() * 2 - 1) * 1.5);

        return new double[] { temperature, rainfall };
    }
}