package ad.auction.dashboard.model.calculator;


import java.util.HashMap;

import ad.auction.dashboard.App;

import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;

/**
 * Manager for managing a given metric
 */
public class MetricManager {

    private final Metrics currentMetric;
    private final HashMap<String, String> allVals = new HashMap<String, String>();

    
    public MetricManager(Metrics currentMetric) {
        this.currentMetric = currentMetric;
        this.buildCampaign();
    }

    /**
     * Calculates the current metric's values for all active campaigns
     */
    public void buildCampaign() {
        var overAll = App.getInstance().controller().runCalculation(currentMetric, MetricFunction.OVERALL);
        overAll.forEach((key, value) -> {
            try {
                while (!value.isDone()) {}
                allVals.put(key, String.format(value.get().toString().contains(".") ? "%.2f" : "%d", value.get()));
            } catch (Exception ignored) {
            }
        });
    }

    public String getCurrentMetric() {
        return currentMetric.getMetric().displayName() + "(" + currentMetric.getMetric().unit() + ")";
    }

    public String getOutput(String campaign) {
        return allVals.get(campaign);
    }
    
}
