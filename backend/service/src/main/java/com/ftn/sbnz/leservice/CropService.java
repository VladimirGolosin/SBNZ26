package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureStatus;
import com.ftn.sbnz.repo.CropRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CropService {

    private final CropRepository repository;

    public CropService(CropRepository repository) {
        this.repository = repository;
    }

    public Crop save(Crop crop) {
        return repository.save(crop);
    }

    public Optional<Crop> findById(Long id) {
        return repository.findById(id);
    }

    public List<Crop> findActiveCrops() {
        return repository.findByStatusIn(List.of(CultureStatus.OK, CultureStatus.INF));
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}