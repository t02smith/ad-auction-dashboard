package ad.auction.dashboard.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.Campaign.CampaignData;
import ad.auction.dashboard.model.files.records.Impression;

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

    /**
     * Opens a new campaign
     * @param name 
     * @return The process that must finish before opening campaign screen
     */
    public Future<Void> openCampaign(String name) {
        return executor.submit(() -> {model.campaigns().openCampaign(name); return null;});
    }

    /**
     * Create a new campaign
     * @param name
     * @param clkPath
     * @param impPath
     * @param svrPath
     */
    public Future<boolean[]> newCampaign(String name, String clkPath, String impPath, String svrPath) {
        return executor.submit(() -> model.campaigns().newCampaign(name, clkPath, impPath, svrPath));
    }

    public void editCampaign(String campaign, String name, String clkPath, String impPath, String svrPath) {
        this.model.campaigns().editCampaign(campaign, name, clkPath, impPath, svrPath);
    }

    public void removeCampaign(String campaign) {
        this.model.campaigns().removeCampaign(campaign);
    }

    /**
     * Run a calculation
     * @param m The metric being calculated
     * @param function The function of that metric
     * @return
     */
    public Future<Object> runCalculation(Metrics m, MetricFunction function) {
        return model.runCalculation(m, function);
    }

    /**
     * Shut down the model
     */
    public void close() {
        this.model.close();
    }

    //Filters

    public void toggleFilter(int hash) {
        this.model.campaigns().toggleFilter(hash);
    }

    public void setDate(boolean start, LocalDateTime value) {
        this.model.campaigns().setDate(start, value);
    }

    public int addImpFilter(Predicate<Impression> pred) {
        return this.model.campaigns().addImpFilter(pred);
    }

    //GETTERS

    public CampaignData getCampaignData() {
        return this.model.campaigns().getCurrentCampaign().getData();
    }

    public List<CampaignData> getCampaigns() {
        return this.model.campaigns().getCampaigns();
    }

}
