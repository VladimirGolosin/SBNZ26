package com.ftn.sbnz.leservice;

import com.ftn.sbnz.repo.CropRepository;
import org.springframework.stereotype.Service;

@Service
public class CropService {

    private final CropRepository repository;

    public CropService(CropRepository repository) {
        this.repository = repository;
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}