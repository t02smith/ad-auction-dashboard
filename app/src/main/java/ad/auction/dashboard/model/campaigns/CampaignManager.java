package ad.auction.dashboard.model.campaigns;

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
 * Used to perform operations concerning campaigns
 * 
 * TODO store campaigns to file
 */
public class CampaignManager {

    private static final String CAMPAIGNS_LOCATION = "./campaigns.xml";

    private static final Logger logger = LogManager.getLogger(CampaignManager.class.getSimpleName());
    private final Model model;

    // Set of loaded campaigns
    private final HashMap<String, Campaign> campaigns = new HashMap<>();

    private Campaign currentCampaign;
    private CampaignHandler handler = new CampaignHandler();

    public CampaignManager(Model model) {
        this.model = model;

        this.handler.parse(CAMPAIGNS_LOCATION);
        this.handler.getCampaigns().forEach(c -> {
            campaigns.put(c.name(), c);
            model.queryFileTracker(FileTrackerQuery.TRACK, c.impressionPath);
            model.queryFileTracker(FileTrackerQuery.TRACK, c.serverPath);
            model.queryFileTracker(FileTrackerQuery.TRACK, c.clickPath);
        });
    }

    // Campaign manager actions
    public enum CMQuery {

        // args = (name, impPath, clkPath, serverPath)
        // returns = void
        NEW_CAMPAIGN,

        // args = void
        // returns = Campaign (current campaing)
        GET_CAMPAIGN,

        // args = void
        // returns = campaign data class (currentCampaign)
        GET_CAMPAIGN_DATA,

        // args = (String)
        // returns = void
        OPEN_CAMPAIGN,

        // args = void
        // returns = List<CampaignData>
        GET_CAMPAIGNS,

        // args = void
        // returns = void
        CLOSE;
    }

    /**
     * Tell the CampaignManager to perform an action
     * 
     * @param query The action to perform
     * @param args  The list of arguments to pass in
     */
    public Optional<Object> query(CMQuery query, String... args) {
        switch (query) {
            case OPEN_CAMPAIGN:
                if (args.length < 1)
                    throw new IllegalArgumentException("Incorrect number of arguments to open campaign");

                this.openCampaign(args[0]);
                return Optional.empty();
            case NEW_CAMPAIGN:
                if (args.length < 4)
                    throw new IllegalArgumentException("Incorrect number of arguments for new campaign");

                this.newCampaign(args[0], args[1], args[2], args[3]);
                return Optional.empty();
            case GET_CAMPAIGN:
                return Optional.of(this.currentCampaign);
            case GET_CAMPAIGN_DATA:
                return Optional.of(this.currentCampaign.getData());
            case GET_CAMPAIGNS:
                return Optional.of(this.campaigns.values().stream()
                        .map(c -> c.getData())
                        .toList());
            case CLOSE:
                this.close();
                return Optional.empty();
        }

        return Optional.empty();
    }

    /**
     * Generate a new campaign to track
     */
    private void newCampaign(String name, String impressionPath, String clickPath, String serverPath) {

        if (campaigns.containsKey(name))
            return;

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
        if (this.currentCampaign == null)
            return;

        if (this.currentCampaign.dataLoaded()) {
            logger.error("Data already loaded for '{}'", this.currentCampaign.name());
            return;
        }

        logger.info("Reading data for campaing '{}'", this.currentCampaign.name());
        var impressions = (Future<List<Impression>>) model
                .queryFileTracker(FileTrackerQuery.READ, currentCampaign.impressionPath).get();
        var clicks = (Future<List<Click>>) model.queryFileTracker(FileTrackerQuery.READ, currentCampaign.clickPath)
                .get();
        var server = (Future<List<Server>>) model.queryFileTracker(FileTrackerQuery.READ, currentCampaign.serverPath)
                .get();

        while (!impressions.isDone() || !clicks.isDone() || !server.isDone()) {
        }

        try {
            currentCampaign.impressions = impressions.get();
            currentCampaign.clicks = clicks.get();
            currentCampaign.server = server.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error reading campaign '{}'", currentCampaign.name());
        }

        currentCampaign.dataLoaded = true;
    }

    /**
     * Opens the specified campaign if it is stored
     * 
     * @param name
     */
    private void openCampaign(String name) {
        if (!this.campaigns.containsKey(name))
            return;
        if (this.currentCampaign != null) {
            if (this.currentCampaign.name().equals(name))
                return;
            logger.info("Flushing data from campaign '{}'", currentCampaign.name());
            this.currentCampaign.flushData();
        }

        logger.info("Opening campaign '{}'", name);
        this.currentCampaign = campaigns.get(name);
        this.loadCampaignData();
    }

    private void close() {
        this.handler.writeToFile(CAMPAIGNS_LOCATION, this.campaigns.values().stream().map(Campaign::getData).toList());
        
    }
}
