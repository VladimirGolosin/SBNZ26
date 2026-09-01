package com.ftn.sbnz.repo;

import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CropRepository extends JpaRepository<Crop, Long> {

    List<Crop> findByStatusIn(List<CultureStatus> statuses);
}