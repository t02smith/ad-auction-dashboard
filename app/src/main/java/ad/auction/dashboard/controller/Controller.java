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

/**
 * The controller class acts as the interface for the 
 * view to interact with the model
 * 
 * @author tcs1g20
 */
public class Controller {

    private final Model model = new Model();

    //Max calculations to be run at once
    private static final int CONTROLLER_THREAD_COUNT = 10;
    private final ExecutorService executor = Executors.newFixedThreadPool(CONTROLLER_THREAD_COUNT);

    /**
     * Opens a new campaign
     * @param name name of the campaign
     * @return The process that must finish before opening campaign screen
     */
    public Future<Void> openCampaign(String name) {
        return executor.submit(() -> {model.campaigns().openCampaign(name); return null;});
    }

    public Future<Void> includeCampaign(String name) {
        return executor.submit(() -> {model.campaigns().includeCampaign(name); return null;});
    }

    /**
     * Create a new campaign
     * @param name name of campaign
     * @param clkPath click log location
     * @param impPath impression log location
     * @param svrPath server log location
     */
    public Future<boolean[]> newCampaign(String name, String clkPath, String impPath, String svrPath) {
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
        this.model.campaigns().editCampaign(campaign, name, clkPath, impPath, svrPath);
    }

    /**
     * Remove a campaign
     * @param campaign the name of the campaign
     */
    public void removeCampaign(String campaign) {
        this.model.campaigns().removeCampaign(campaign);
    }

    /**
     * Run a calculation
     * @param m The metric being calculated
     * @param function The function of that metric
     * @return the result of the calculation
     */
    public HashMap<String, Future<Object>> runCalculation(Metrics m, MetricFunction function) {
        return model.runCalculation(m, function);
    }

    /**
     * Shut down the model
     */
    public void close() {
        this.model.close();
    }

    //Filters

    /**
     * Toggle a filter on/off
     * @param hash The hash of the filter returned when adding it initially
     */
    public void toggleFilter(int hash) {
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
     * Add another user filter
     * @param predicate the filter
     * @return the hash of the filter
     */
    public int addUserFilter(Predicate<User> predicate) {
        return this.model.campaigns().addUserFilter(predicate);
    }

    //GETTERS

    public CampaignData getCampaignData() {
        return this.model.campaigns().getCurrentCampaign().getData();
    }

    public List<CampaignData> getCampaigns() {
        return this.model.campaigns().getCampaigns();
    }

    public CampaignData getCampaignData(String name) {return this.model.campaigns().getCampaignData(name);}
}
