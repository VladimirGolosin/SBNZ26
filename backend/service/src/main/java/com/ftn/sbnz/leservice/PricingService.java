package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.ActionName;
import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.model.SolutionName;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class PricingService {

    private final Map<ActionName, Double> actionCosts = new EnumMap<>(ActionName.class);
    private final Map<SolutionName, Double> solutionCosts = new EnumMap<>(SolutionName.class);
    private final Map<CultureName, Double> pricePerSqm = new EnumMap<>(CultureName.class);

    public PricingService() {
        actionCosts.put(ActionName.WEED_REMOVAL, 8.0);
        actionCosts.put(ActionName.FERTILIZATION, 15.0);
        actionCosts.put(ActionName.IRRIGATION, 5.0);
        actionCosts.put(ActionName.HILLING, 10.0);
        actionCosts.put(ActionName.PEST_CONTROL, 18.0);
        actionCosts.put(ActionName.PRUNING, 12.0);
        actionCosts.put(ActionName.TYING, 6.0);
        actionCosts.put(ActionName.TREE_WHITEWASHING, 14.0);
        actionCosts.put(ActionName.COPPER_SULFATE_SPRAY, 20.0);

        solutionCosts.put(SolutionName.APPLY_FUNGICIDE, 18.0);
        solutionCosts.put(SolutionName.APPLY_INSECTICIDE, 16.0);
        solutionCosts.put(SolutionName.APPLY_FERTILIZATION, 15.0);
        solutionCosts.put(SolutionName.REMOVE_INFECTED_LEAVES, 7.0);
        solutionCosts.put(SolutionName.REMOVE_INFECTED_FRUITS, 7.0);
        solutionCosts.put(SolutionName.PRUNE_INFECTED_BRANCHES, 10.0);
        solutionCosts.put(SolutionName.ADD_PROTECTION, 12.0);

        pricePerSqm.put(CultureName.ONION, 1.20);
        pricePerSqm.put(CultureName.BEANS, 1.50);
        pricePerSqm.put(CultureName.TOMATO, 1.80);
        pricePerSqm.put(CultureName.POTATO, 0.90);
        pricePerSqm.put(CultureName.ZUCCINI, 1.10);
        pricePerSqm.put(CultureName.CORN, 0.70);
        pricePerSqm.put(CultureName.CHERRY, 3.50);
        pricePerSqm.put(CultureName.APPLE, 3.00);
        pricePerSqm.put(CultureName.PLUM, 2.80);
        pricePerSqm.put(CultureName.WATERMELON, 1.30);
        pricePerSqm.put(CultureName.GRAPE, 4.00);
    }

    public double getActionCost(ActionName action) {
        return actionCosts.getOrDefault(action, 10.0);
    }

    public double getSolutionCost(SolutionName solution) {
        return solutionCosts.getOrDefault(solution, 12.0);
    }

    public double getPricePerSqm(CultureName culture) {
        return pricePerSqm.getOrDefault(culture, 1.50);
    }
}