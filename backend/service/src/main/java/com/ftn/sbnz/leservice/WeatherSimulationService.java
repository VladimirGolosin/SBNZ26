package com.ftn.sbnz.leservice;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

@Service
public class WeatherSimulationService {

    public enum Profile {
        NORMAL,
        DROUGHT,
        RAINY
    }

    private static class ProfileModifier {
        double temperatureOffset;
        double rainfallMultiplier;

        ProfileModifier(double temperatureOffset, double rainfallMultiplier) {
            this.temperatureOffset = temperatureOffset;
            this.rainfallMultiplier = rainfallMultiplier;
        }
    }

    private static final double OPTIMAL_TEMPERATURE = 22.0;
    private static final double OPTIMAL_WEEKLY_RAINFALL = 23.3;
    private static final double DAILY_RAINFALL_BASE = OPTIMAL_WEEKLY_RAINFALL / 7.0;

    private final Map<Profile, ProfileModifier> profileModifiers = new EnumMap<>(Profile.class);
    private final Random random = new Random();

    private Profile activeProfile = Profile.NORMAL;

    public WeatherSimulationService() {
        profileModifiers.put(Profile.NORMAL, new ProfileModifier(0.0, 1.0));
        profileModifiers.put(Profile.DROUGHT, new ProfileModifier(18.0, 0.05));
        profileModifiers.put(Profile.RAINY, new ProfileModifier(-3.0, 1.8));
    }

    public void setActiveProfile(Profile profile) {
        this.activeProfile = profile;
    }

    public Profile getActiveProfile() {
        return activeProfile;
    }

    public void reset() {
        activeProfile = Profile.NORMAL;
    }

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
        ProfileModifier modifier = profileModifiers.get(activeProfile);

        double temperature = OPTIMAL_TEMPERATURE + seasonalTemperatureOffset(month) + modifier.temperatureOffset
                + (random.nextDouble() * 2 - 1) * 2.0;

        double rainfall = Math.max(0, DAILY_RAINFALL_BASE * seasonalRainfallMultiplier(month) * modifier.rainfallMultiplier
                + (random.nextDouble() * 2 - 1) * 1.5);

        temperature = Math.round(temperature * 1000.0) / 1000.0;
        rainfall = Math.round(rainfall * 1000.0) / 1000.0;

        return new double[] { temperature, rainfall };
    }
}