package ad.auction.dashboard.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.Campaign.CampaignData;
import ad.auction.dashboard.model.files.records.User;
import ad.auction.dashboard.view.settings.Themes;

/**
 * The controller class acts as the interface for the 
 * view to interact with the model
 * 
 * @author tcs1g20
 */
public class Controller {

    private final Model model = new Model();

    private boolean available = true;

    /**
     * Checks whether the controller has been closed
     * @throws RuntimeException if the controller is unavailable
     */
    public void isAvailable() throws RuntimeException {
        if (!available)
            throw new RuntimeException("Controller has been closed");
    }

    //Max calculations to be run at once
    private static final int CONTROLLER_THREAD_COUNT = 10;
    private final ExecutorService executor = Executors.newFixedThreadPool(CONTROLLER_THREAD_COUNT);

    /**
     * Opens a new campaign
     * @param name name of the campaign
     * @return The process that must finish before opening campaign screen
     */
    public Future<Void> openCampaign(String name) {
        this.isAvailable();
        return executor.submit(() -> {model.campaigns().openCampaign(name); return null;});
    }

    /**
     * Includes/excludes a campaign
     * @param name the campaign to be toggled
     * @return when the process is finished
     */
    public Future<Void> toggleCampaign(String name) {
        this.isAvailable();
        return executor.submit(() -> {
            if (!model.campaigns().isIncluded(name)) model.campaigns().includeCampaign(name);
            else model.campaigns().unincludeCampaign(name);
            return null;
        });
    }

    /**
     * Create a new campaign
     * @param name name of campaign
     * @param clkPath click log location
     * @param impPath impression log location
     * @param svrPath server log location
     */
    public Future<boolean[]> newCampaign(String name, String clkPath, String impPath, String svrPath) {
        this.isAvailable();
        return executor.submit(() -> model.campaigns().newCampaign(name, clkPath, impPath, svrPath));
    }

    /**
     * Edit the details of a campaign
     * If something isn't being changed leave the empty string ""
     * @param campaign the name of the campaign being edited
     * @param name new campaign name
     * @param clkPath new click log location
     * @param impPath new impression log location
     * @param svrPath new server log location
     */
    public void editCampaign(String campaign, String name, String clkPath, String impPath, String svrPath) {
        this.isAvailable();
        this.model.campaigns().editCampaign(campaign, name, clkPath, impPath, svrPath);
    }

    /**
     * Remove a campaign
     * @param campaign the name of the campaign
     */
    public void removeCampaign(String campaign) {
        this.isAvailable();
        this.model.campaigns().removeCampaign(campaign);
    }

    /**
     * Runs a calculation on all active campaigns
     * @param m The metric to run
     * @param function The type of calculation
     * @return a map (campaign name, process running the calculation)
     */
    public HashMap<String, Future<Object>> runCalculation(Metrics m, MetricFunction function) {
        this.isAvailable();
        return model.runCalculation(m, function);
    }

    /**
     * Shut down the model
     */
    public void close() {
        this.isAvailable();
        this.available = false;
        this.model.close();
    }

    //CONFIG

    /**
     * Sets the metric loaded initially when opening a campaign
     * @param m the new default metric
     */
    public void setDefaultMetric(Metrics m) {
        this.isAvailable();
        this.model.setDefaultMetric(m);
    }

    /**
     * Set the number of points calculated per time unit
     * @param factor new factor value
     */
    public void setFactor(int factor) {
        this.isAvailable();
        this.model.setFactor(factor);
    }

    /**
     * Sets the current application theme
     * @param theme the new theme
     */
    public void setTheme(Themes theme) {
        this.isAvailable();
        model.setTheme(theme);
    }

    //FILTERS

    /**
     * Toggle a filter on/off
     * @param hash The hash of the filter returned when adding it initially
     */
    public void toggleFilter(int hash) {
        this.isAvailable();
        this.model.campaigns().toggleFilter(hash);
    }

    /**
     * Set the start/end date of the data to be shown
     * @param start changing start(true) or end(false)
     * @param value The new date
     */
    public void setDate(boolean start, LocalDateTime value) {
        this.model.campaigns().setDate(start, value);
    }

    /**
     * Sets whether a graph is a cumulative or trend graph
     * @param state cumulative(true) or trend(false)
     */
    public void setCumulative(boolean state) {
        this.isAvailable();
        this.model.setCumulative(state);
    }

    //SNAPSHOTS

    /**
     * Generate a new snapshot of the current filter settings
     * @return The name of the snapshot
     */
    public String snapshot() {
        this.isAvailable();
        return this.model.campaigns().snapshotCampaign();
    }

    /**
     * Remove an active snapshot
     * @param name the snapshots name returned upon creation
     */
    public void removeSnapshot(String name) {
        this.isAvailable();
        this.model.campaigns().removeSnapshot(name);
    }

    /**
     * Add another user filter
     * @param predicate the filter
     * @return the hash of the filter
     */
    public int addUserFilter(Predicate<User> predicate) {
        this.isAvailable();
        return this.model.campaigns().addUserFilter(predicate);
    }

    //GETTERS

    /**
     * returns current campaigns data record
     * @return The current campaigns data record
     */
    public CampaignData getCampaignData() {
        this.isAvailable();
        return this.model.campaigns().getCurrentCampaign().getData();
    }

    /**
     * returns data records for all campaigns
     * @return Gets data records for all campaigns
     */
    public List<CampaignData> getCampaigns() {
        this.isAvailable();
        return this.model.campaigns().getCampaigns();
    }

    /**
     * returns list of data records for active campaigns
     * @return Gets data records for active campaigns
     */
    public List<CampaignData> getActiveCampaigns() {
        this.isAvailable();
        return this.model.campaigns().getActiveCampaignData();
    }

    /**
     * Get the data record for a specific campaign
     * @param name the campaign to retrieve
     * @return the campaign's data record
     */
    public CampaignData getCampaignData(String name) {
        this.isAvailable();
        return this.model.campaigns().getCampaignData(name);
    }

    /**
     * @return The active application theme
     */
    public Themes getTheme() {
        this.isAvailable();
        return model.theme();
    }

    /**
     * @return the number of points calculated per time unit
     */
    public int getFactor() {
        this.isAvailable();
        return this.model.getFactor();
    }

    /**
     * Gets the default metric that is calculated when a campaign is opened
     * @return the default metric
     */
    public Metrics getDefaultMetric() {
        this.isAvailable();
        return this.model.getDefaultMetric();
    }
}
