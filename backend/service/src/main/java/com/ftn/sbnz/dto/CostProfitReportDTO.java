package com.ftn.sbnz.dto;

import java.util.ArrayList;
import java.util.List;

public class CostProfitReportDTO {

    private List<CostProfitEntryDTO> entries = new ArrayList<>();
    private double totalCost;
    private double totalRevenue;
    private double totalProfit;

    public List<CostProfitEntryDTO> getEntries() {
        return entries;
    }

    public void setEntries(List<CostProfitEntryDTO> entries) {
        this.entries = entries;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }
}