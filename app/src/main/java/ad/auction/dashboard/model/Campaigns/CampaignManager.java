package ad.auction.dashboard.model.Campaigns;

import java.util.*;
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

    public CampaignManager(Model model) {
        this.model = model;
    }

    //Campaign manager actions
    public enum CMQuery {
        NEW_CAMPAIGN, LOAD_CAMPAIGN, GET_CAMPAIGN;
    }

    /**
     * Get all campaign from campaign manager
     * @return all campaign sorted in ascending order by name
     */
    public List<Campaign> getAllCampaign() {
        ArrayList<Campaign> list = new ArrayList<>(campaigns.values());
        list.sort(Comparator.comparing(Campaign::name));
        return list;
    }


    /**
     * Tell the CampaignManager to perform an action
     *
     * @param query The action to perform
     * @param args  The list of arguments to pass in
     */
    public Optional<Campaign> query(CMQuery query, String... args) {
        switch (query) {
            case NEW_CAMPAIGN:
                if (args.length < 4)
                    throw new IllegalArgumentException("Incorrect number of arguments for new campaign");

                this.newCampaign(args[0], args[1], args[2], args[3]);
                return Optional.empty();
            case LOAD_CAMPAIGN:
                if (args.length < 1)
                    throw new IllegalArgumentException("Incorrect number of arguments for new campaign");

                this.loadCampaignData(args[0]);
                return Optional.empty();
            case GET_CAMPAIGN:
                if (args.length < 1)
                    throw new IllegalArgumentException("Incorrect number of arguments for new campaign");

                if (!this.campaigns.containsKey(args[0]))
                    throw new IllegalArgumentException("Campaign '" + args[0] + "' not found");

                return Optional.of(this.campaigns.get(args[0]));
        }

        return Optional.empty();
    }

    /**
     * Generate a new campaign to track
     */
    public void newCampaign(String name, String impressionPath, String clickPath, String serverPath) {
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
    public void loadCampaignData(String campaign) {
        if (!this.campaigns.containsKey(campaign)) return;

        var c = this.campaigns.get(campaign);
        if (c.dataLoaded()) {
            logger.error("Data already loaded for '{}'", campaign);
            return;
        }

        logger.info("Reading data for campaing '{}'", campaign);
        var impressions = (Future<List<Impression>>) model.queryFileTracker(FileTrackerQuery.READ, c.impressionPath).get();
        var clicks = (Future<List<Click>>) model.queryFileTracker(FileTrackerQuery.READ, c.clickPath).get();
        var server = (Future<List<Server>>) model.queryFileTracker(FileTrackerQuery.READ, c.serverPath).get();

        while (!impressions.isDone() || !clicks.isDone() || !server.isDone()) {
        }

        try {
            c.impressions = impressions.get();
            c.clicks = clicks.get();
            c.server = server.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error reading campaign '{}'", c.name());
        }


        c.dataLoaded = true;

    }

}
