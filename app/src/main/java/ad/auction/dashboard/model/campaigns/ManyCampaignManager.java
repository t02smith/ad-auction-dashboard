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

    public ManyCampaignManager(Model model) {
        super(model);
    }

    public void includeCampaign(String campaign) {
        if (!this.campaigns.containsKey(campaign)) throw new IllegalArgumentException("Campaign " + campaign + " not found");
        if (this.includedCampaigns.contains(campaign)) return;

        logger.info("Including campaign {}", campaign);
        this.includedCampaigns.add(campaign);
        this.loadCampaignData(campaign);
        //reload...

    }

    public void unincludeCampaing(String name) {
        if (!this.includedCampaigns.contains(name)) return;

        logger.info("unincluding campaign {}", name);
        var c = this.campaigns.get(name);
        c.flushData();
        this.includedCampaigns.remove(name);
    }

    public HashMap<String, Campaign> getActiveCampaigns() {
        var cs = new HashMap<String, Campaign>();
        includedCampaigns.forEach(c -> cs.put(c, this.campaigns.get(c)));
        cs.put(currentCampaign.name(), currentCampaign);
        return cs;
    }

    public boolean isIncluded(String name) {
        return this.includedCampaigns.contains(name);
    }


}
