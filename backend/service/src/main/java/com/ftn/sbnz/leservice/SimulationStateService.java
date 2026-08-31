package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.SimulationState;
import com.ftn.sbnz.repo.SimulationStateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SimulationStateService {

    private static final Long SINGLETON_ID = 1L;

    private final SimulationStateRepository repository;

    public SimulationStateService(SimulationStateRepository repository) {
        this.repository = repository;
    }

    public Optional<LocalDate> getCurrentDate() {
        return repository.findById(SINGLETON_ID).map(SimulationState::getCurrentDate);
    }

    public void saveCurrentDate(LocalDate date) {
        SimulationState state = repository.findById(SINGLETON_ID).orElse(new SimulationState());
        state.setId(SINGLETON_ID);
        state.setCurrentDate(date);
        repository.save(state);
    }
}