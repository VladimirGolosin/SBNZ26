package com.ftn.sbnz.leservice;

import com.ftn.sbnz.dto.CostProfitEntryDTO;
import com.ftn.sbnz.dto.CostProfitReportDTO;
import com.ftn.sbnz.model.Action;
import com.ftn.sbnz.model.Crop;
import com.ftn.sbnz.model.CultureStatus;
import com.ftn.sbnz.model.Problem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final CropService cropService;
    private final PricingService pricingService;

    public ReportService(CropService cropService, PricingService pricingService) {
        this.cropService = cropService;
        this.pricingService = pricingService;
    }

    public CostProfitReportDTO generateCostProfitReport(Long userId, int year) {
        List<Crop> crops = new ArrayList<>();
        crops.addAll(cropService.findCropsForUser(userId, true));
        crops.addAll(cropService.findCropsForUser(userId, false));

        List<CostProfitEntryDTO> entries = new ArrayList<>();
        double totalCost = 0;
        double totalRevenue = 0;

        for (Crop crop : crops) {
            if (crop.getPlantedDate() == null || crop.getPlantedDate().getYear() != year) {
                continue;
            }

            double cost = 0;
            for (Action action : crop.getActions()) {
                cost += pricingService.getActionCost(action.getName());
            }
            for (Problem problem : crop.getProblems()) {
                if (problem.getProvidedSolution() != null && problem.getAddressed() != null) {
                    cost += pricingService.getSolutionCost(problem.getProvidedSolution());
                }
            }

            double pricePerSqm = pricingService.getPricePerSqm(crop.getCultureName());

            double revenue = 0;
            if (crop.getStatus() == CultureStatus.COLLECTED) {
                revenue = crop.getSize() * pricePerSqm;
            } else if (crop.getStatus() == CultureStatus.INF_COLLECTED) {
                revenue = 0.5 * crop.getSize() * pricePerSqm;
            }

            double profit = revenue - cost;

            entries.add(new CostProfitEntryDTO(crop.getId(), crop.getCultureName(), crop.getStatus(), crop.getSize(), pricePerSqm, cost, revenue, profit));
            totalCost += cost;
            totalRevenue += revenue;
        }

        CostProfitReportDTO report = new CostProfitReportDTO();
        report.setEntries(entries);
        report.setTotalCost(totalCost);
        report.setTotalRevenue(totalRevenue);
        report.setTotalProfit(totalRevenue - totalCost);
        return report;
    }
}