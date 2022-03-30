package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.model.Model;

import java.util.HashMap;
import java.util.HashSet;

public class ManyCampaignManager extends CampaignManager {

    //Campaigns which are to be included
    private final HashSet<String> includedCampaigns = new HashSet<>();

    public ManyCampaignManager(Model model) {
        super(model);
    }

    public void includeCampaign(String campaign) {
        if (!this.campaigns.containsKey(campaign)) throw new IllegalArgumentException("Campaign " + campaign + " not found");
        if (this.includedCampaigns.contains(campaign)) return;

        this.includedCampaigns.add(campaign);
        this.loadCampaignData(campaign);
        //reload...

    }

    public HashMap<String, Campaign> getActiveCampaigns() {
        var cs = new HashMap<String, Campaign>();
        includedCampaigns.forEach(c -> cs.put(c, this.campaigns.get(c)));
        cs.put(currentCampaign.name(), currentCampaign);
        return cs;
    }


}
