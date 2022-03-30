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
    protected final Model model;

    // Set of loaded campaigns
    protected final HashMap<String, FilteredCampaign> campaigns = new HashMap<>();

    protected FilteredCampaign currentCampaign;
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

    protected void loadCampaignData() {
        this.loadCampaignData(this.currentCampaign.name());
    }

    /**
     * Load the data into the campaign class for the calculations
     */
    protected void loadCampaignData(String campaign) {
        var c = this.campaigns.get(campaign);

        if (c == null)
            return;

        if (c.dataLoaded()) {
            logger.error("Data already loaded for '{}'", c.name());
            return;
        }

        logger.info("Reading data for campaign '{}'", c.name());

        var impressions = this.model.files().readFile(c.getData().impPath());
        var clicks = this.model.files().readFile(c.getData().clkPath());
        var server = this.model.files().readFile(c.getData().svrPath());

        while (!impressions.isDone() || !clicks.isDone() || !server.isDone()) {
        }

        try {
            c.setImpressions(impressions.get().stream().map(i -> (Impression)i).toList());
            c.setClicks(clicks.get().stream().map(clk -> (Click)clk).toList());
            c.setServer(server.get().stream().map(svr -> (Server)svr).toList());

            var ls = c.impressionsLs();

            c.setDate(true, ls.get(0).dateTime());
            c.setDate(false, ls.get(ls.size()-1).dateTime());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        c.dataLoaded = c.impressions != null
            && c.clicks != null
            && c.server != null;

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
    protected void cleanFiles() {
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
