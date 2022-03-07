package ad.auction.dashboard.controller;

import java.util.Optional;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.Campaigns.CampaignManager.CMQuery;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;

public class Controller {

    private final Model model = new Model();

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
        GET_CAMPAIGN_DATA;
    }

    public Optional<Object> query(ControllerQuery query, Object... args) {
        switch (query) {
            case OPEN_CAMPAIGN:
                model.queryCampaignManager(CMQuery.OPEN_CAMPAIGN, (String) args[0]);
                return Optional.empty();
            case NEW_CAMPAIGN:
                model.queryCampaignManager(CMQuery.NEW_CAMPAIGN, (String) args[0], (String) args[1], (String) args[2],
                        (String) args[3]);
                return Optional.empty();
            case CALCULATE:
                return Optional.of(model.runCalculation((Metrics) args[0], (MetricFunction) args[1]));
            case GET_CAMPAIGN_DATA:
                return Optional.of(model.queryCampaignManager(CMQuery.GET_CAMPAIGN_DATA));
        }

        return Optional.empty();
    }
}
