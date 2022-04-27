package ad.auction.dashboard.model.campaigns;

import java.util.HashMap;

/**
 * A snapshot is a configuration containing:
 * - source campaign
 * - filters changed from default
 *
 * It will allow us to display and dynamically update many graphs at once
 * @author tcs1g20
 */
public class CampaignSnapshot extends FilteredCampaign {

    private static int ID_COUNTER = 0;

    /**
     * Generate a new campaign snapshot
     * @param c the campaign to snapshot
     */
    @SuppressWarnings("unchecked")
    public CampaignSnapshot(FilteredCampaign c) {
        super (c.name + ":"+ID_COUNTER++, c.impPath, c.clkPath, c.svrPath);
        this.dataLoaded = true;

        this.impressions = c.impressions;
        this.clicks = c.clicks;
        this.server = c.server;
        this.userData = c.userData;

        this.start = c.start;
        this.end = c.end;

        this.filterActive = (HashMap<Integer, Boolean>) c.filterActive.clone();
        this.userFilters = c.userFilters;
    }

    /**
     * @param filterHash The filter to toggle
     * @throws RuntimeException snapshot filters cannot be toggled
     */
    @Override
    public void toggleFilter(int filterHash) {
        throw new RuntimeException("Cannot toggle filters on a snapshot");
    }

    /**
     * @param state on/off
     * @throws RuntimeException snapshot filters cannot be toggled
     */
    @Override
    public void toggleAllFilters(boolean state) {
        throw new RuntimeException("Cannot toggle filters on a snapshot");
    }
}
