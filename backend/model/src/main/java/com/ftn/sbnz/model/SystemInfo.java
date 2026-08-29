package com.ftn.sbnz.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "system_info")
public class SystemInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(targetClass = CultureName.class)
    @CollectionTable(
            name = "system_info_recommended_cultures",
            joinColumns = @JoinColumn(name = "system_info_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "culture_name")
    private List<CultureName> recommendedCultures = new ArrayList<>();

    private boolean doPlantsNeedExtraWater;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "system_info_id")
    private List<WeatherDayInfo> weather = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CultureName> getRecommendedCultures() {
        return recommendedCultures;
    }

    public void setRecommendedCultures(List<CultureName> recommendedCultures) {
        this.recommendedCultures = recommendedCultures;
    }

    public boolean isDoPlantsNeedExtraWater() {
        return doPlantsNeedExtraWater;
    }

    public void setDoPlantsNeedExtraWater(boolean doPlantsNeedExtraWater) {
        this.doPlantsNeedExtraWater = doPlantsNeedExtraWater;
    }

    public List<WeatherDayInfo> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherDayInfo> weather) {
        this.weather = weather;
    }
}
