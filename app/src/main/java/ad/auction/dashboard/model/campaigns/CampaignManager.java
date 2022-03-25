package ad.auction.dashboard.model.campaigns;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

import ad.auction.dashboard.model.files.records.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.campaigns.Campaign.CampaignData;
import ad.auction.dashboard.model.files.FileType;
import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;

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

    private FilteredCampaign currentCampaign;
    private final CampaignHandler handler = new CampaignHandler();

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
     * Removes a given campaign
     * @param name the name of a campaign
     */
    public void removeCampaign(String name) {
        if (this.campaigns.containsKey(name)) {
            this.campaigns.remove(name);
            logger.info("Removing campaign {}", name);
        }
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

        logger.info("Reading data for campaign '{}'", this.currentCampaign.name());

        var impressions = this.model.files().readFile(currentCampaign.getData().impPath());
        var clicks = this.model.files().readFile(currentCampaign.getData().clkPath());
        var server = this.model.files().readFile(currentCampaign.getData().svrPath());

        while (!impressions.isDone() || !clicks.isDone() || !server.isDone()) {
        }

        try {
            currentCampaign.setImpressions(impressions.get().stream().map(i -> (Impression)i).toList());
            currentCampaign.setClicks(clicks.get().stream().map(c -> (Click)c).toList());
            currentCampaign.setServer(server.get().stream().map(c -> (Server)c).toList());

            var ls = currentCampaign.impressionsLs();

            currentCampaign.setDate(true, ls.get(0).dateTime());
            currentCampaign.setDate(false, ls.get(ls.size()-1).dateTime());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        currentCampaign.dataLoaded = currentCampaign.impressions != null
            && currentCampaign.clicks != null
            && currentCampaign.server != null;

    }

    /**
     * Opens the specified campaign if it is store
     * @param name The campaigns name
     */
    public void openCampaign(String name) {
        if (!this.campaigns.containsKey(name))
            return;
        if (this.currentCampaign != null) {
            if (this.currentCampaign.name().equals(name))
                return;
            logger.info("Flushing data from campaign '{}'", currentCampaign.name());
            this.currentCampaign.flushData();
            logger.info("testing");
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

    /**
     * Edit a campaigns details
     * If properties are unchanged then null should be passed
     * @param campaign name of the campaign
     * @param name new name
     * @param clkPath new clicks path
     * @param impPath new impressions path
     * @param svrPath new server path
     */
    public void editCampaign(String campaign, String name, String clkPath, String impPath, String svrPath) {

        if (!campaigns.containsKey(campaign)) {
            logger.error("Campaign '{}' not found", campaign);
            return;
        }

        var c = campaigns.get(campaign);
        if (name != null) {
            synchronized (this.campaigns) {
                this.campaigns.remove(campaign);
                this.campaigns.put(name, c);
                logger.info(name);
            }
            c.name = name;

        }
        if (!clkPath.isEmpty()) c.clkPath = clkPath;
        if (!impPath.isEmpty()) c.impPath = impPath;
        if (!svrPath.isEmpty()) c.svrPath = svrPath;

        logger.info("Successfully updated campaign {}", campaign);
    }

    /**
     * Close the campaign manager
     */
    public void close() {
        this.handler.writeToFile(CAMPAIGNS_LOCATION, this.campaigns.values().stream().map(Campaign::getData).toList());
    }

    // FILTERS

    /**
     * Toggles a filter on/off
     * @param hash The filters identifier
     */
    public void toggleFilter(int hash) {
        this.currentCampaign.toggleFilter(hash);
    }

    /**
     * Set the start/end date of the data to be shown
     * @param start changing start(true) or end(false)
     * @param value The new date
     */
    public void setDate(boolean start, LocalDateTime value) throws IllegalArgumentException {
        this.currentCampaign.setDate(start,value);
    }

    /**
     * Add a new filter on the impression data type
     * @param usrFilter the impressions filter
     * @return the filter's hash
     */
    public int addUserFilter(Predicate<User> usrFilter) {
        return this.currentCampaign.addUserFilter(usrFilter);
    }

    // GETTERS

    public FilteredCampaign getCurrentCampaign() {
        return this.currentCampaign;
    }

    public CampaignData getCampaignData(String name) {
        if (!this.campaigns.containsKey(name)) return null;

        return this.campaigns.get(name).getData();
    }

    public List<CampaignData> getCampaigns() {
        return this.campaigns.values().stream().map(Campaign::getData).toList();
    }

}
