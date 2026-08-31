package com.ftn.sbnz.repo;

import com.ftn.sbnz.model.SimulationState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulationStateRepository extends JpaRepository<SimulationState, Long> {
}