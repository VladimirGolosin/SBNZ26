package com.ftn.sbnz.dto;

import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.model.CultureStatus;

public class CostProfitEntryDTO {

    private Long cropId;
    private CultureName cultureName;
    private CultureStatus status;
    private int size;
    private double cost;
    private double revenue;
    private double profit;

    public CostProfitEntryDTO() {
    }

    public CostProfitEntryDTO(Long cropId, CultureName cultureName, CultureStatus status, int size, double cost, double revenue, double profit) {
        this.cropId = cropId;
        this.cultureName = cultureName;
        this.status = status;
        this.size = size;
        this.cost = cost;
        this.revenue = revenue;
        this.profit = profit;
    }

    public Long getCropId() {
        return cropId;
    }

    public void setCropId(Long cropId) {
        this.cropId = cropId;
    }

    public CultureName getCultureName() {
        return cultureName;
    }

    public void setCultureName(CultureName cultureName) {
        this.cultureName = cultureName;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}