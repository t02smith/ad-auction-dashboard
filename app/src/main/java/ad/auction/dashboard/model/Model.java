package ad.auction.dashboard.model;

import java.util.Optional;
import java.util.concurrent.Future;

import ad.auction.dashboard.model.calculator.Calculator;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.Campaign;
import ad.auction.dashboard.model.campaigns.CampaignManager;
import ad.auction.dashboard.model.campaigns.CampaignManager.CMQuery;
import ad.auction.dashboard.model.files.FileTracker;
import ad.auction.dashboard.model.files.FileTracker.FileTrackerQuery;

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
     * Allows the controller to prompt the model to perform file actions
     * @param query The chosen command
     * @param target The target file
     * @return The result if any
     */
    public Optional<Object> queryFileTracker(FileTrackerQuery query, String filename) {
        return this.fileTracker.query(query, filename);
    }

    /**
     * Run a calculation for the current open campaign
     * @param metric
     * @param func
     * @return
     */
    public Future<Object> runCalculation(Metrics metric, MetricFunction func) {
        var campaign = (Campaign)queryCampaignManager(CMQuery.GET_CAMPAIGN).get();
        return this.calculator.runCalculation(campaign, metric, func);
    }

    /**
     * Query the campaign manager
     * @param query
     * @param args
     * @return
     */
    public Optional<Object> queryCampaignManager(CMQuery query, String... args) {
        return campaignManager.query(query, args);
    }
    
    public void close() {
        this.campaignManager.query(CMQuery.CLOSE);
    }

}
