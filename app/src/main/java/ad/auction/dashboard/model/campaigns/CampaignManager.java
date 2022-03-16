package ad.auction.dashboard.model.campaigns;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.campaigns.Campaign.CampaignData;
import ad.auction.dashboard.model.files.FileType;
import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;
import ad.auction.dashboard.model.files.records.SharedFields;

/**
 * Used to perform operations concerning campaigns
 *
 */
public class CampaignManager {

    private static final String CAMPAIGNS_LOCATION = "./campaigns.xml";

    private static final Logger logger = LogManager.getLogger(CampaignManager.class.getSimpleName());
    private final Model model;

    // Set of loaded campaigns
    private final HashMap<String, FilteredCampaign> campaigns = new HashMap<>();

    private Campaign currentCampaign;
    private CampaignHandler handler = new CampaignHandler();

    public CampaignManager(Model model) {
        this.model = model;

        this.handler.parse(CAMPAIGNS_LOCATION);
        this.handler.getCampaigns().forEach(c -> {
            campaigns.put(c.name(), c);
            model.files().trackFile(c.getData().impPath());
            model.files().trackFile(c.getData().clkPath());
            model.files().trackFile(c.getData().svrPath());
        });
    }

    /**
     * Generate a new campaign to track
     */
    public boolean[] newCampaign(String name, String clickPath, String impressionPath, String serverPath) {
        this.cleanFiles();

        if (campaigns.containsKey(name))
            throw new IllegalArgumentException("Campaign already exists");

        logger.info("Creating new campaign '{}'", name);

        this.model.files().trackFile(impressionPath);
        this.model.files().trackFile(clickPath);
        this.model.files().trackFile(serverPath);

        var output = new boolean[] {
                this.model.files().correctFileType(clickPath, FileType.CLICK),
                this.model.files().correctFileType(impressionPath, FileType.IMPRESSION),
                this.model.files().correctFileType(serverPath, FileType.SERVER)
        };

        if (output[0] && output[1] && output[2]) {
            this.campaigns.put(name, new FilteredCampaign(name, impressionPath, clickPath, serverPath));
            logger.info("Campaign '{}' successfully created", name);
        } else {
            logger.error("Incorrect file formats submitted for campaign '{}'", name);
        }

        return output;

    }

    /**
     * Load the data into the campaign class for the calculations
     */
    private void loadCampaignData() {
        if (this.currentCampaign == null)
            return;

        if (this.currentCampaign.dataLoaded()) {
            logger.error("Data already loaded for '{}'", this.currentCampaign.name());
            return;
        }

        logger.info("Reading data for campaing '{}'", this.currentCampaign.name());

        try {
            var impressions = this.model.files().readFile(currentCampaign.getData().impPath());
            var clicks = this.model.files().readFile(currentCampaign.getData().clkPath());
            var server = this.model.files().readFile(currentCampaign.getData().svrPath());

            while (!impressions.isDone() || !clicks.isDone() || !server.isDone()) {
            }

            currentCampaign.impressions = impressions.get().stream().map(i -> (Impression)i).toList();
            currentCampaign.clicks = clicks.get().stream().map(c -> (Click)c).toList();
            currentCampaign.server = server.get().stream().map(c -> (Server)c).toList();;

            currentCampaign.dataLoaded = true;
    
        } catch (IOException e) {
            logger.error("Error reading/processing data files: {}", e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error fetching processed data: {}", e.getMessage());
        }        
    }

    /**
     * Opens the specified campaign if it is stored
     * 
     * @param name
     */
    public void openCampaign(String name) {
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

    /**
     * Will untrack any files not currently needed for any campaign
     */
    private void cleanFiles() {
        HashSet<String> files = new HashSet<>();

        campaigns.values().forEach(c -> {
            files.add(c.clkPath);
            files.add(c.impPath);
            files.add(c.svrPath);
        });

        this.model.files().clean(files);
    }

    // TODO finish edit campaign
    public void editCampaign(String campaign, String name, String clkPath, String impPath, String svrPath) {

        if (!campaigns.containsKey(campaign)) {
            logger.error("Campaign '{}' not found", campaign);
            return;
        }

        var c = campaigns.get(campaign);

        if (name != null)
            c.name = name;
    }

    /**
     * Close the campaign manager
     */
    public void close() {
        this.handler.writeToFile(CAMPAIGNS_LOCATION, this.campaigns.values().stream().map(Campaign::getData).toList());
    }

    // FILTERS

    @SuppressWarnings({"unchecked","rawtypes"})
    public void addFilter(String campaign, FileType type, Predicate filter) {
        if (!campaigns.containsKey(campaign)) {
            logger.error("Campaign '{}' not found", campaign);
            return;
        }

        FilteredCampaign c = campaigns.get(campaign);
        if (type == null) c.addFilter((Predicate<SharedFields>)filter);
        else {
            switch (type) {
                case IMPRESSION:
                    c.addImpFilter((Predicate<Impression>) filter);
                default:
                    throw new IllegalArgumentException("No specific filter for " + type);
            }
        }
        
    }

    // GETTERS

    public Campaign getCurrentCampaign() {
        return this.currentCampaign;
    }

    public List<CampaignData> getCampaigns() {
        return this.campaigns.values().stream().map(Campaign::getData).toList();
    }
}
