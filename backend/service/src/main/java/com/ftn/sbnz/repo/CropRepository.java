package com.ftn.sbnz.repo;

import com.ftn.sbnz.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {
}