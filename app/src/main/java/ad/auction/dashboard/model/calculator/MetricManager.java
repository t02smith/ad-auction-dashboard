package ad.auction.dashboard.model.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;

public class MetricManager {

    private final Controller controller;

    private Metrics currentMetric;
    private MetricFunction metricFunction = MetricFunction.OVERALL;

    private Set<String> activeCampaigns;
    private HashMap<String, String> allVals = new HashMap<String, String>();


    
    public MetricManager(Controller controller, Metrics currentMetric) {
        this.controller = controller;
        this.currentMetric = currentMetric;
        this.buildCampaign();
    }

    public void buildCampaign() {
        var overAll = this.controller.runCalculation(currentMetric, metricFunction);
        activeCampaigns = overAll.keySet();
        overAll.forEach((key, value) -> {
            try {
                allVals.put(key, String.format(value.get().toString().contains(".") ? "%.2f" : "%d", value.get()));
            } catch (Exception ignored) {
            }
        });
    }

    public String getValue(String campaignName) {
        return activeCampaigns.contains(campaignName) ? allVals.get(campaignName) : "";
    }

    public String getCurrentMetric() {
        return currentMetric.getMetric().displayName() + "(" + currentMetric.getMetric().unit() + ")";
    }
    public Set<String> getActiveCampaigns() {
        return activeCampaigns;
    }

    public void setActiveCampaigns(Set<String> activeCampaigns) {
        this.activeCampaigns = activeCampaigns;
    }

    public Controller getController() {
        return controller;
    }


    public MetricFunction getMetricFunction() {
        return metricFunction;
    }

    public void setMetricFunction(MetricFunction metricFunction) {
        this.metricFunction = metricFunction;
    }
    
}
