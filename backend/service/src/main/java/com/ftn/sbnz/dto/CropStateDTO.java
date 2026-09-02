package com.ftn.sbnz.dto;

import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.model.CultureStatus;
import com.ftn.sbnz.model.Recommendation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CropStateDTO {

    private Long id;
    private CultureName cultureName;
    private int level;
    private CultureStatus status;
    private int size;
    private LocalDate plantedDate;
    private List<Recommendation> recommendations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CultureName getCultureName() {
        return cultureName;
    }

    public void setCultureName(CultureName cultureName) {
        this.cultureName = cultureName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public CultureStatus getStatus() {
        return status;
    }

    public void setStatus(CultureStatus status) {
        this.status = status;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public LocalDate getPlantedDate() {
        return plantedDate;
    }

    public void setPlantedDate(LocalDate plantedDate) {
        this.plantedDate = plantedDate;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }
}