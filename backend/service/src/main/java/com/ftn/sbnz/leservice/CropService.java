package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.model.CultureStatus;
import com.ftn.sbnz.repo.CropRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CropService {

    private final CropRepository repository;
    private final CultureReferenceService cultureReferenceService;

    private final Map<CultureName, Integer> maxLevels = new EnumMap<>(CultureName.class);

    public CropService(CropRepository repository, CultureReferenceService cultureReferenceService) {
        this.repository = repository;
        this.cultureReferenceService = cultureReferenceService;

        maxLevels.put(CultureName.ONION, 3);
        maxLevels.put(CultureName.BEANS, 3);
        maxLevels.put(CultureName.POTATO, 4);
        maxLevels.put(CultureName.TOMATO, 4);
        maxLevels.put(CultureName.ZUCCINI, 3);
        maxLevels.put(CultureName.CORN, 3);
        maxLevels.put(CultureName.GRAPE, 4);
        maxLevels.put(CultureName.WATERMELON, 3);
    }

    public int getMaxLevel(CultureName cultureName) {
        return maxLevels.getOrDefault(cultureName, 3);
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

    public Crop collectCrop(Long cropId, LocalDate currentDate, boolean confirmed) {
        Crop crop = repository.findById(cropId)
                .orElseThrow(() -> new IllegalArgumentException("Crop not found: " + cropId));

        if (!crop.isActive()) {
            throw new IllegalStateException("Crop is not active and cannot be collected (current status: " + crop.getStatus() + ")");
        }

        Month harvestMonth = cultureReferenceService.getHarvestMonth(crop.getCultureName());
        if (currentDate.getMonth().compareTo(harvestMonth) < 0) {
            throw new IllegalStateException("Crop cannot be collected before its harvest month (" + harvestMonth + ")");
        }

        boolean reachedMaxLevel = crop.getLevel() >= getMaxLevel(crop.getCultureName());

        if (crop.getStatus() == CultureStatus.OK && !reachedMaxLevel && !confirmed) {
            throw new IllegalStateException("This crop has not completed all growth stages (level " + crop.getLevel() + "/" + getMaxLevel(crop.getCultureName()) + "). Collecting now will mark it as INF. Confirm to proceed anyway.");
        }

        if (crop.getStatus() == CultureStatus.INF || !reachedMaxLevel) {
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