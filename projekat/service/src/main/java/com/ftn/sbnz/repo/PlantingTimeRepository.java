package com.ftn.sbnz.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.sbnz.model.models.PlantingTime;

@Repository
public interface PlantingTimeRepository extends JpaRepository<PlantingTime, Long> {
    
    PlantingTime findByCulture(String culture);
}

