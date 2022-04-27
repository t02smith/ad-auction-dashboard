package ad.auction.dashboard.model;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.Future;

import ad.auction.dashboard.model.calculator.Calculator;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.Campaign;
import ad.auction.dashboard.model.campaigns.ManyCampaignManager;
import ad.auction.dashboard.model.config.ConfigHandler;
import ad.auction.dashboard.model.files.FileTracker;
import ad.auction.dashboard.view.settings.Themes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Central class for model components
 * Used by the Controller to interact with the backend
 * 
 * @author tcs1g20
 */
public class Model {

    private static final Logger logger = LogManager.getLogger(Model.class.getSimpleName());

    //Tracks and reads data files
    private final FileTracker fileTracker = new FileTracker();

    //Performs calculations given certain metrics
    private final Calculator calculator = new Calculator();

    //Manages different user campaigns
    private final ManyCampaignManager campaignManager = new ManyCampaignManager(this);

    //Config settings
    private static final String CONFIG_LOCATION = "./config.xml";

    private Themes theme = Themes.DARK;
    private int factor = 2;

    /**
     * Construct a new model
     * Attempts to read from the config file
     */
    public Model() {
        var config = fetchConfig();
        if (config == null) return;

        if (config.defaultMetric() != null)
            this.calculator.setDefaultMetric(config.defaultMetric());

        if (config.theme() != null)
            this.theme = config.theme();

        if (config.factor() != null)
            this.factor = config.factor();

        if (config.campaigns() != null) {
            this.campaignManager.setCampaigns(config.campaigns());
            config.campaigns().forEach(c -> {
                fileTracker.trackFile(c.clkPath());
                fileTracker.trackFile(c.impPath());
                fileTracker.trackFile(c.svrPath());
            });
        }

    }

    /**
     * Get the config settings from the file
     * @return config settings
     */
    private ConfigHandler.Config fetchConfig() {
        var handler = new ConfigHandler();
        try {
            handler.parse(CONFIG_LOCATION);
            return handler.getConfig();
        } catch (IOException e) {
            return null;
        }

    }

    /*CALCULATOR*/

    /**
     * Run a calculation on all included campaigns
     * @param metric the metric to calculate
     * @param func what to calculate
     * @return map - campaignName, result
     */
    public HashMap<String, Future<Object>> runCalculation(Metrics metric, MetricFunction func) {
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

    /**
     * Whether the data is cumulative or per time unit
     * @param state cumulative data or not
     */
    public void setCumulative(boolean state) {
        this.campaignManager.getActiveCampaigns().values().forEach(Campaign::clearCache);
        this.calculator.setCumulative(state);
    }

    /**
     * Set the time resolution
     * @param res new resolution
     */
    public void setTimeResolution(ChronoUnit res) {
        this.calculator.setTimeResolution(res);
    }

    //config setters

    /**
     * Set the metric to be shown initially
     * @param m the initially shown metric
     */
    public void setDefaultMetric(Metrics m) {
        this.calculator.setDefaultMetric(m);
    }

    /**
     * How many points per time unit
     * e.g. 2 -> 2 points per day
     * @param factor points per time unit
     */
    public void setFactor(int factor) {
        logger.info("Setting factor {} -> {}", this.factor, factor);
        this.factor = factor;
        this.campaignManager.clearCache();
    }

    /**
     * Set the current view's theme
     * @param theme new theme
     */
    public void setTheme(Themes theme) {
        logger.info("Setting theme {} -> {}", this.theme, theme);
        this.theme = theme;
    }

    /*GETTERS*/

    /**
     * @return the campaign manager
     */
    public ManyCampaignManager campaigns() {
        return this.campaignManager;
    }

    /**
     * @return the file tracker
     */
    public FileTracker files() {
        return this.fileTracker;
    }

    /**
     * @return the default metric loaded when a campaign is opened
     */
    public Metrics getDefaultMetric() {
        return this.calculator.getDefaultMetric();
    }

    /**
     * @return the current application theme
     */
    public Themes theme() {
        return this.theme;
    }

    /**
     * @return the number of points generated per time res
     */
    public int getFactor() {
        return this.factor;
    }

    /*UTILITY*/

    /**
     * Close the model down
     */
    public void close() {
        var handler = new ConfigHandler();
        handler.writeToFile("./config.xml", new ConfigHandler.Config(
                this.calculator.getDefaultMetric(),
                this.theme,
                this.factor,
                this.campaignManager.getCampaigns()
        ));
        this.campaignManager.closeAll();
    }

}
