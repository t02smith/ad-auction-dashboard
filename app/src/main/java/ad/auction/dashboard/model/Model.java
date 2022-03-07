package ad.auction.dashboard.model;

import java.util.Optional;
import java.util.concurrent.Future;

import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.Campaigns.CampaignManager;
import ad.auction.dashboard.model.Campaigns.CampaignManager.CMQuery;
import ad.auction.dashboard.model.calculator.Calculator;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.files.FileTracker;
import ad.auction.dashboard.model.files.FileTracker.FileTrackerQuery;


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
     * Run a given calculation
     * @param c
     * @param metric
     * @param func
     * @return
     */
    public Future<Object> runCalculation(Campaign c, Metrics metric, MetricFunction func) {
        return calculator.runCalculation(c, metric, func);
    }

    public Future<Object> runCalculation(String campaignName, Metrics metric, MetricFunction func) {
        var campaign = queryCampaignManager(CMQuery.GET_CAMPAIGN, campaignName).get();
        return this.runCalculation(campaign, metric, func);
    }

    /**
     * Query the campaign manager
     * @param query
     * @param args
     * @return
     */
    public Optional<Campaign> queryCampaignManager(CMQuery query, String... args) {
        return campaignManager.query(query, args);
    }
    

}
