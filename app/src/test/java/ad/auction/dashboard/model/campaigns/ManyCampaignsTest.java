package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.model.Model;
import org.junit.jupiter.api.AfterAll;
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

    @AfterAll
    public static void tearDownAll() {
        model.campaigns().closeAll();
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

    /*SNAPSHOTS*/

    @Test
    @DisplayName("Store snapshot")
    public void storeSnapshotTest() {
        var hash = model.campaigns().snapshotCampaign();
        assertNotNull(model.campaigns().getSnapshot(hash));
        model.campaigns().removeSnapshot(hash);
    }

    @Test
    @DisplayName("Remove snapshot")
    public void removeSnapshotTest() {
        var hash = model.campaigns().snapshotCampaign();
        model.campaigns().removeSnapshot(hash);
        assertNull(model.campaigns().getSnapshot(hash));
    }

    @Test
    @DisplayName("Snapshot with no open campaign")
    public void snapshotWithoutOpenCampaign() {
        model.campaigns().closeCurrentCampaign();
        assertThrows(IllegalStateException.class,
                () -> model.campaigns().snapshotCampaign());
        model.campaigns().openCampaign("2 week - test");
    }

    @Test
    @DisplayName("Snapshot with same filters")
    public void snapshotWithSameFilters() {
        model.campaigns().addUserFilter(u -> true);
        var hash = model.campaigns().snapshotCampaign();
        assertThrows(IllegalStateException.class,
                () -> model.campaigns().snapshotCampaign());
        model.campaigns().removeSnapshot(hash);
    }
}
