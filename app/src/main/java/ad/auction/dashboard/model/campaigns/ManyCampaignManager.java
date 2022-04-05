package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;

public class ManyCampaignManager extends CampaignManager {

    private static final Logger logger = LogManager.getLogger(ManyCampaignManager.class.getSimpleName());

    //Campaigns which are to be included
    private final HashSet<String> includedCampaigns = new HashSet<>();

    //Additional campaigns/filtered datasets to include
    private final HashMap<Integer, CampaignSnapshot> snapshots = new HashMap<>();

    public ManyCampaignManager(Model model) {
        super(model);
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
    public int snapshotCampaign() {
        logger.info("Snapshotting configuration of " + currentCampaign.name());
        var snap = this.currentCampaign.generateSnapshot();
        var hash = snap.hashCode();
        this.snapshots.put(hash,snap);
        return hash;
    }

    /**
     * Remove a campaign from the included list
     * @param campaign the campaign to remove
     */
    public void unincludeCampaing(String campaign) {
        if (!this.includedCampaigns.contains(campaign)) return;

        logger.info("unincluding campaign {}", campaign);
        var c = this.campaigns.get(campaign);
        c.flushData();
        this.includedCampaigns.remove(campaign);
    }

    public HashMap<String, Campaign> getActiveCampaigns() {
        var cs = new HashMap<String, Campaign>();
        includedCampaigns.forEach(c -> cs.put(c, this.campaigns.get(c)));
        snapshots.forEach((hash, s) -> cs.put(hash.toString(), s));
        cs.put(currentCampaign.name(), currentCampaign);
        return cs;
    }

    /**
     * Whether a campaign is included
     * @param name the campaign's name
     * @return whether the campaign is included
     */
    public boolean isIncluded(String name) {
        return this.includedCampaigns.contains(name);
    }


}
