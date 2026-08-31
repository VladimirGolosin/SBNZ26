package com.ftn.sbnz.repo;

import com.ftn.sbnz.model.PredefinedWeather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredefinedWeatherRepository extends JpaRepository<PredefinedWeather, Long> {
}