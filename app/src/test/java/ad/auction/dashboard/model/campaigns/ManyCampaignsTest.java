package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManyCampaignsTest {

    private static final Model model = new Model();

    @BeforeAll
    public static void setUp() {
        model.campaigns().newCampaign("2 week - test",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");
        model.campaigns().openCampaign("2 week - test");
        model.campaigns().newCampaign("new test",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");
        model.campaigns().includeCampaign("new test");
    }

    @Test
    @DisplayName("include a campaign")
    public void includeCampaignsTest() {
        assertTrue(model.campaigns().isIncluded("new test"));
    }

    @Test
    @DisplayName("uninclude a campaign")
    public void unincludeCampaignTest() {
        model.campaigns().unincludeCampaing("new test");
        assertFalse(model.campaigns().isIncluded("new test"));
        model.campaigns().includeCampaign("new test");
    }

    @Test
    @DisplayName("check active campaign list")
    public void checkActiveCampaignTest() {
        var ls = model.campaigns().getActiveCampaigns();
        assertTrue(ls.containsKey("2 week - test"));
        assertTrue(ls.containsKey("new test"));
    }
}
