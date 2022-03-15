package ad.auction.dashboard.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.CampaignManager.CMQuery;

/**
 * The controller class acts as the interface for the 
 * view to interact with the model
 * 
 * It works by the user passing a query and a set of 
 * arguments
 * 
 * @author tcs1g20
 */
public class Controller {

    private final Model model = new Model();

        //Max calculations to be ran at once
        private static final int CONTROLLER_THREAD_COUNT = 10;
        private final ExecutorService executor = Executors.newFixedThreadPool(CONTROLLER_THREAD_COUNT);

    public enum ControllerQuery {
        // args = (String campaignName)
        // returns = void
        OPEN_CAMPAIGN,

        // args = (String campaignName, String impressionPath, String impressionPath,
        // String clickPath, String serverPath)
        // returns = void
        NEW_CAMPAIGN,

        // args = (Metrics metric, MetricFunction function)
        // returns = Future<Object>
        CALCULATE,

        // args = void
        // returns = CampaignData
        GET_CAMPAIGN_DATA,

        // args = void
        // returns List<CampaignData>
        GET_CAMPAIGNS,

        // args = void
        // returns = void
        CLOSE;
    }

    /**
     * Query the model
     * @param query
     * @param args
     * @return
     */
    public Future<Object> query(ControllerQuery query, Object... args) {
        Callable<Object> c = () -> null;

        switch (query) {
            case OPEN_CAMPAIGN:
                c = () -> model.queryCampaignManager(CMQuery.OPEN_CAMPAIGN, (String) args[0]);
                break;
            case NEW_CAMPAIGN:
                c = () -> model.queryCampaignManager(CMQuery.NEW_CAMPAIGN, (String) args[0], (String) args[1], (String) args[2],
                        (String) args[3]);
                        break;
            case CALCULATE:
                return model.runCalculation((Metrics) args[0], (MetricFunction) args[1]);
            case GET_CAMPAIGN_DATA:
                c = () -> model.queryCampaignManager(CMQuery.GET_CAMPAIGN_DATA).get();
                break;
            case GET_CAMPAIGNS:
                c = () -> model.queryCampaignManager(CMQuery.GET_CAMPAIGNS).get();
                break;
            case CLOSE:
                c = () -> {this.model.close(); return null;};
                break;
        }

        return executor.submit(c);
    }

}
