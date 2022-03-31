package ad.auction.dashboard.model;

import java.util.HashMap;
import java.util.concurrent.Future;

import ad.auction.dashboard.model.calculator.Calculator;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.ManyCampaignManager;
import ad.auction.dashboard.model.files.FileTracker;

/**
 * Central class for model components
 * Used by the Controller to interact with the backend
 * 
 * @author tcs1g20
 */
public class Model {

    //Tracks and reads data files
    private final FileTracker fileTracker = new FileTracker();

    //Performs calculations given certain metrics
    private final Calculator calculator = new Calculator();

    //Manages different user campaigns
    private final ManyCampaignManager campaignManager = new ManyCampaignManager(this);

    /**
     * Run a calculation for the current open campaign
     * @param metric The calculation
     * @param func The function to calculate for the metric
     * @return The result of the calculation
     */
    public HashMap<String, Future<Object>> runCalculation(Metrics metric, MetricFunction func) {
        var campaigns = this.campaignManager.getActiveCampaigns();
        var res = new HashMap<String, Future<Object>>();

        //Only one histogram is visible at any one time
        if (func == MetricFunction.HISTOGRAM) {
            res.put(campaignManager.getCurrentCampaign().name(), this.calculator.runCalculation(campaignManager.getCurrentCampaign(), metric, func));
            return res;
        }

        campaigns.forEach((key, value) -> res.put(key, this.calculator.runCalculation(value, metric, func)));
        return res;
    }

    public void setCumulative(boolean state) {
        this.campaignManager.getCurrentCampaign().clearCache();
        this.calculator.setCumulative(state);
    }

    public ManyCampaignManager campaigns() {
        return this.campaignManager;
    }

    public FileTracker files() {
        return this.fileTracker;
    }
    
    public void close() {
        this.campaignManager.close();
    }

}
