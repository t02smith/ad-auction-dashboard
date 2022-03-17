package ad.auction.dashboard.model;

import java.util.concurrent.Future;

import ad.auction.dashboard.model.calculator.Calculator;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.CampaignManager;
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
    private final CampaignManager campaignManager = new CampaignManager(this);

    /**
     * Run a calculation for the current open campaign
     * @param metric The calculation
     * @param func The function to calculate for the metric
     * @return The result of the calculation
     */
    public Future<Object> runCalculation(Metrics metric, MetricFunction func) {
        var campaign = this.campaignManager.getCurrentCampaign();
        return this.calculator.runCalculation(campaign, metric, func);
    }

    public CampaignManager campaigns() {
        return this.campaignManager;
    }

    public FileTracker files() {
        return this.fileTracker;
    }
    
    public void close() {
        this.campaignManager.close();
    }

}
