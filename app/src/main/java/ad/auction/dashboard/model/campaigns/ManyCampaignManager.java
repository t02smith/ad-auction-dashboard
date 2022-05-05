package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.campaigns.Campaign.CampaignData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Campaign manager to handle many open campaigns including:
 *  - other campaigns
 *  - snapshot campaigns
 */
public class ManyCampaignManager extends CampaignManager {

    private static final Logger logger = LogManager.getLogger(ManyCampaignManager.class.getSimpleName());

    //Campaigns which are to be included
    private final HashSet<String> includedCampaigns = new HashSet<>();

    //Additional campaigns/filtered datasets to include
    private final HashMap<String, CampaignSnapshot> snapshots = new HashMap<>();

    /**
     * Create a new ManyCampaignManager
     * @param model the model this manager is a part of
     */
    public ManyCampaignManager(Model model) {
        super(model);
    }

    /**
     * Open a new campaign
     * Clears included and snapshot campaigns
     * @param name The campaigns name
     */
    @Override
    public void openCampaign(String name) {
        logger.info("Clearing included & snapshot campaigns");
        this.includedCampaigns.clear();
        this.snapshots.clear();

        super.openCampaign(name);
    }

    @Override
    public void setDateSpan(boolean start, int span) {
        super.setDateSpan(start, span);
        this.includedCampaigns.forEach(c -> {
            var camp = campaigns.get(c);
            camp.setRelativeDate(start,span);
            camp.clearCache();
        });
    }

    /**
     * Remove a campaign
     * If it is included then the data is flushed first
     * @param name the name of a campaign
     */
    @Override
    public void removeCampaign(String name) {
        if (this.campaigns.containsKey(name)) {
            if (includedCampaigns.contains(name)) {
                this.campaigns.get(name).flushData();
            }
            this.campaigns.remove(name);
            logger.info("Removing campaign {}", name);
        } else throw new NoSuchElementException("No campaign " + name + " found");
    }

    /**
     * Includes a new campaign alongside the current one
     * @param campaign the campaign to include
     */
    public void includeCampaign(String campaign) {
        if (!this.campaigns.containsKey(campaign)) throw new IllegalArgumentException("Campaign " + campaign + " not found");
        if (this.includedCampaigns.contains(campaign)) return;

        logger.info("Including campaign {}", campaign);
        this.includedCampaigns.add(campaign);
        this.loadCampaignData(campaign);
        //reload...

    }

    /**
     * Create a snapshot of the current campaign configuration
     * @return a reference hash
     */
    public String snapshotCampaign() {
        if (this.currentCampaign == null)
            throw new IllegalStateException("A campaign must be opened before snapshotting");

        logger.info("Snapshotting configuration of " + currentCampaign.name());
        var snap = this.currentCampaign.generateSnapshot();

        this.snapshots.values().forEach(ss -> {
             if (ss.filterActive.equals(snap.filterActive)) {
                 logger.error("Snapshot already taken of these filters");
                 throw new IllegalStateException("Snapshot already taken of these filters");
             }
        });

        this.snapshots.put(snap.name(),snap);
        return snap.name();
    }

    /**
     * Remove a given snapshot
     * @param name the name of the snapshot returned when created
     */
    public void removeSnapshot(String name) {
        logger.info("Removing snapshot {}", name);
        this.snapshots.remove(name);
    }

    /**
     * Remove a campaign from the included list
     * @param campaign the campaign to remove
     */
    public void unincludeCampaign(String campaign) {
        if (!this.includedCampaigns.contains(campaign)) return;

        logger.info("unincluding campaign {}", campaign);
        var c = this.campaigns.get(campaign);
        c.flushData();
        this.includedCampaigns.remove(campaign);
    }

    /**
     * @return active campaign (campaign name, campaign object)
     */
    public HashMap<String, Campaign> getActiveCampaigns() {
        var cs = new HashMap<String, Campaign>();
        includedCampaigns.forEach(c -> cs.put(c, this.campaigns.get(c)));
        cs.putAll(snapshots);
        cs.put(currentCampaign.name(), currentCampaign);
        return cs;
    }

    /**
     * @return active campaign data records
     */
    public List<CampaignData> getActiveCampaignData() {
        Collection<Campaign> res = this.getActiveCampaigns().values();
        List<CampaignData> data = new ArrayList<>();
        res.forEach(c -> data.add(c.getData()));
        return data;
    }

    /**
     * Whether a campaign is included
     * @param name the campaign's name
     * @return whether the campaign is included
     */
    public boolean isIncluded(String name) {
        return this.includedCampaigns.contains(name);
    }

    /**
     * @param name the snapshot's name
     * @return the snapshot object
     */
    public CampaignSnapshot getSnapshot(String name) {
        return this.snapshots.get(name);
    }

}
