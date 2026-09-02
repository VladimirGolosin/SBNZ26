package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.model.CultureStatus;
import com.ftn.sbnz.repo.CropRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
public class CropService {

    private final CropRepository repository;
    private final CultureReferenceService cultureReferenceService;

    public CropService(CropRepository repository, CultureReferenceService cultureReferenceService) {
        this.repository = repository;
        this.cultureReferenceService = cultureReferenceService;
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

    public List<Crop> findActiveCropsByCulture(CultureName cultureName) {
        return repository.findByCultureNameAndStatusIn(cultureName, List.of(CultureStatus.OK, CultureStatus.INF));
    }

    public List<Crop> findCropsForUser(Long userId, boolean active) {
        List<CultureStatus> statuses = active
                ? List.of(CultureStatus.OK, CultureStatus.INF)
                : List.of(CultureStatus.FAILED, CultureStatus.COLLECTED, CultureStatus.INF_COLLECTED, CultureStatus.ABANDONED);
        return repository.findByUserIdAndStatusIn(userId, statuses);
    }

    public Crop collectCrop(Long cropId, LocalDate currentDate) {
        Crop crop = repository.findById(cropId)
                .orElseThrow(() -> new IllegalArgumentException("Crop not found: " + cropId));

        if (!crop.isActive()) {
            throw new IllegalStateException("Crop is not active and cannot be collected (current status: " + crop.getStatus() + ")");
        }

        Month harvestMonth = cultureReferenceService.getHarvestMonth(crop.getCultureName());
        if (currentDate.getMonth().compareTo(harvestMonth) < 0) {
            throw new IllegalStateException("Crop cannot be collected before its harvest month (" + harvestMonth + ")");
        }

        if (crop.getStatus() == CultureStatus.INF) {
            crop.setStatus(CultureStatus.INF_COLLECTED);
        } else {
            crop.setStatus(CultureStatus.COLLECTED);
        }

        return repository.save(crop);
    }

    public Crop failCrop(Long cropId) {
        Crop crop = repository.findById(cropId)
                .orElseThrow(() -> new IllegalArgumentException("Crop not found: " + cropId));

        if (!crop.isActive()) {
            throw new IllegalStateException("Crop is not active and cannot be marked as failed (current status: " + crop.getStatus() + ")");
        }

        crop.setStatus(CultureStatus.FAILED);
        return repository.save(crop);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}