package ad.auction.dashboard.model.Campaigns;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.files.FileTracker.FileTrackerQuery;
import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;

/**
 * TODO store campaigns and load from file
 */
public class CampaignManager {

    private static final Logger logger = LogManager.getLogger(CampaignManager.class.getSimpleName());
    private final Model model;

    //Set of loaded campaigns
    private final HashMap<String, Campaign> campaigns = new HashMap<>();

    private Campaign currentCampaign;

    public CampaignManager(Model model) {
        this.model = model;
    }

    //Campaign manager actions
    public enum CMQuery {
        NEW_CAMPAIGN,
        GET_CAMPAIGN,
        GET_CAMPAIGN_DATA,
        OPEN_CAMPAIGN;
    }

    /**
     * Tell the CampaignManager to perform an action
     * @param query The action to perform
     * @param args The list of arguments to pass in
     */
    public Optional<Object> query(CMQuery query, String... args) {
        switch (query) {
            case OPEN_CAMPAIGN:
                if (args.length < 1)
                    throw new IllegalArgumentException("Incorrect number of arguments to open campaign");

                this.openCampaign(args[0]);
                return Optional.of(this.currentCampaign);
            case NEW_CAMPAIGN:
                if (args.length < 4)
                    throw new IllegalArgumentException("Incorrect number of arguments for new campaign");

                this.newCampaign(args[0], args[1], args[2], args[3]);
                this.openCampaign(args[0]);
                return Optional.empty();
            case GET_CAMPAIGN:
                return Optional.of(this.currentCampaign);
            case GET_CAMPAIGN_DATA:
                return Optional.of(this.currentCampaign.getData());
        }

        return Optional.empty();
    }

    /**
     * Generate a new campaign to track
     */
    private void newCampaign(String name, String impressionPath, String clickPath, String serverPath) {
        if (campaigns.containsKey(name)) return;

        logger.info("Creating new campaign '{}'", name);
        this.campaigns.put(name, new Campaign(name, impressionPath, clickPath, serverPath));
        this.model.queryFileTracker(FileTrackerQuery.TRACK, impressionPath);
        this.model.queryFileTracker(FileTrackerQuery.TRACK, clickPath);
        this.model.queryFileTracker(FileTrackerQuery.TRACK, serverPath);
    }

    /**
     * Load the data into the campaign class for the calculations
     */
    @SuppressWarnings("unchecked")
    private void loadCampaignData() {
        if (this.currentCampaign == null) return;

        if (this.currentCampaign.dataLoaded()) {
            logger.error("Data already loaded for '{}'", this.currentCampaign.name());
            return;
        }

        logger.info("Reading data for campaing '{}'", this.currentCampaign.name());
        var impressions = (Future<List<Impression>>)model.queryFileTracker(FileTrackerQuery.READ, currentCampaign.impressionPath).get();
        var clicks = (Future<List<Click>>)model.queryFileTracker(FileTrackerQuery.READ, currentCampaign.clickPath).get();
        var server = (Future<List<Server>>)model.queryFileTracker(FileTrackerQuery.READ, currentCampaign.serverPath).get();

        while (!impressions.isDone() || !clicks.isDone() || !server.isDone()) {}

        try {
            currentCampaign.impressions = impressions.get();
            currentCampaign.clicks = clicks.get();
            currentCampaign.server = server.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error reading campaign '{}'", currentCampaign.name());
        }
        
        currentCampaign.dataLoaded = true;
    }

    private void openCampaign(String name) {
        if (!this.campaigns.containsKey(name)) return;
        if (this.currentCampaign != null) {
            if (this.currentCampaign.name().equals(name)) return;
            logger.info("Flushing data from campaign '{}'", currentCampaign.name());
            this.currentCampaign.flushData();
        }

        logger.info("Opening campaign '{}'", name);
        this.currentCampaign = campaigns.get(name);
        this.loadCampaignData();
    }

}
