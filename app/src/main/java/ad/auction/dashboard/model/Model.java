package ad.auction.dashboard.model;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import ad.auction.dashboard.model.calculator.Calculator;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.ManyCampaignManager;
import ad.auction.dashboard.model.config.ConfigHandler;
import ad.auction.dashboard.model.config.Guide;
import ad.auction.dashboard.model.config.GuideHandler;
import ad.auction.dashboard.model.files.FileTracker;
import ad.auction.dashboard.view.settings.Themes;

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

    private Themes theme = Themes.DARK;

    public Model() {
        var config = fetchConfig();

        if (config.defaultMetric() != null)
            this.calculator.setDefaultMetric(config.defaultMetric());

        if (config.theme() != null)
            this.theme = config.theme();

        if (config.campaigns() != null) {
            this.campaignManager.setCampaigns(config.campaigns());
            config.campaigns().forEach(c -> {
                fileTracker.trackFile(c.clkPath());
                fileTracker.trackFile(c.impPath());
                fileTracker.trackFile(c.svrPath());
            });
        }

    }

    private ConfigHandler.Config fetchConfig() {
        var handler = new ConfigHandler();
        handler.parse("./config.xml");
        return handler.getConfig();
    }

    /*CALCULATOR*/

    public HashMap<String, Future<Object>> runCalculation(Metrics metric, MetricFunction func) {
        return runCalculation(metric, func, 1);
    }

    /**
     * Run a calculation for the current open campaign
     * @param metric The calculation
     * @param func The function to calculate for the metric
     * @return The result of the calculation
     */
    public HashMap<String, Future<Object>> runCalculation(Metrics metric, MetricFunction func, int factor) {
        var campaigns = this.campaignManager.getActiveCampaigns();
        var res = new HashMap<String, Future<Object>>();

        //Only one histogram is visible at any one time
        if (func == MetricFunction.HISTOGRAM) {
            res.put(campaignManager.getCurrentCampaign().name(), this.calculator.runCalculation(campaignManager.getCurrentCampaign(), metric, func, factor));
            return res;
        }

        campaigns.forEach((key, value) -> res.put(key, this.calculator.runCalculation(value, metric, func, factor)));
        return res;
    }

    public void setCumulative(boolean state) {
        this.campaignManager.getCurrentCampaign().clearCache();
        this.calculator.setCumulative(state);
    }

    public void setTimeResolution(ChronoUnit res) {
        this.calculator.setTimeResolution(res);
    }

    public void setDefaultMetric(Metrics m) {
        this.calculator.setDefaultMetric(m);
    }

    public void setTheme(Themes theme) {
        this.theme = theme;
    }

    /*GETTERS*/

    public ManyCampaignManager campaigns() {
        return this.campaignManager;
    }

    public FileTracker files() {
        return this.fileTracker;
    }

    public Metrics getDefaultMetric() {
        return this.calculator.getDefaultMetric();
    }

    public Themes theme() {
        return this.theme;
    }

    /*UTILITY*/
    
    public void close() {
        var handler = new ConfigHandler();
        handler.writeToFile("./config.xml", new ConfigHandler.Config(
                this.calculator.getDefaultMetric(),
                this.theme,
                this.campaignManager.getCampaigns()
        ));
    }

}
