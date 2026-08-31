package com.ftn.sbnz.repo;

import com.ftn.sbnz.model.WeatherDayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherDayInfoRepository extends JpaRepository<WeatherDayInfo, Long> {

    List<WeatherDayInfo> findTop7ByOrderByDateDesc();
}