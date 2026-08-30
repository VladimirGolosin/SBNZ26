package com.ftn.sbnz.repo;

import com.ftn.sbnz.model.WeatherDayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDayInfoRepository extends JpaRepository<WeatherDayInfo, Long> {
}