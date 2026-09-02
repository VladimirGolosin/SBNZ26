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
        actionCosts.put(ActionName.WEED_REMOVAL, 5.0);
        actionCosts.put(ActionName.FERTILIZATION, 8.0);
        actionCosts.put(ActionName.IRRIGATION, 3.0);
        actionCosts.put(ActionName.HILLING, 6.0);
        actionCosts.put(ActionName.PEST_CONTROL, 10.0);
        actionCosts.put(ActionName.PRUNING, 7.0);
        actionCosts.put(ActionName.TYING, 4.0);
        actionCosts.put(ActionName.TREE_WHITEWASHING, 9.0);
        actionCosts.put(ActionName.COPPER_SULFATE_SPRAY, 12.0);

        solutionCosts.put(SolutionName.APPLY_FUNGICIDE, 10.0);
        solutionCosts.put(SolutionName.APPLY_INSECTICIDE, 9.0);
        solutionCosts.put(SolutionName.APPLY_FERTILIZATION, 8.0);
        solutionCosts.put(SolutionName.REMOVE_INFECTED_LEAVES, 4.0);
        solutionCosts.put(SolutionName.REMOVE_INFECTED_FRUITS, 4.0);
        solutionCosts.put(SolutionName.PRUNE_INFECTED_BRANCHES, 6.0);
        solutionCosts.put(SolutionName.ADD_PROTECTION, 7.0);

        pricePerSqm.put(CultureName.ONION, 3.5);
        pricePerSqm.put(CultureName.BEANS, 4.0);
        pricePerSqm.put(CultureName.TOMATO, 4.5);
        pricePerSqm.put(CultureName.POTATO, 2.5);
        pricePerSqm.put(CultureName.ZUCCINI, 3.0);
        pricePerSqm.put(CultureName.CORN, 2.0);
        pricePerSqm.put(CultureName.CHERRY, 6.0);
        pricePerSqm.put(CultureName.APPLE, 5.5);
        pricePerSqm.put(CultureName.PLUM, 5.0);
        pricePerSqm.put(CultureName.WATERMELON, 3.5);
        pricePerSqm.put(CultureName.GRAPE, 7.0);
    }

    public double getActionCost(ActionName action) {
        return actionCosts.getOrDefault(action, 5.0);
    }

    public double getSolutionCost(SolutionName solution) {
        return solutionCosts.getOrDefault(solution, 5.0);
    }

    public double getPricePerSqm(CultureName culture) {
        return pricePerSqm.getOrDefault(culture, 3.0);
    }
}