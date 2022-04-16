package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.model.Model;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@Tag("model/campaigns")
public class CampaignSnapshotTest {

    private static final Model model = new Model();

    @BeforeAll
    public static void setUp() {
        model.campaigns().newCampaign("2 week - test",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");
        model.campaigns().openCampaign("2 week - test");
    }

    @AfterAll
    public static void tearDownAll() {
        model.campaigns().closeAll();
    }

    /*SNAPSHOT CREATION*/

    @Test
    @DisplayName("Correct campaign snapshot")
    public void correctSnapshotTest() {
        var hash = model.campaigns().snapshotCampaign();
        var s = model.campaigns().getSnapshot(hash);

        assertEquals("2 week - test:1", s.name());
        assertEquals("./data/2-week/click_log.csv", s.clkPath);
        assertEquals("./data/2-week/impression_log.csv", s.impPath);
        assertEquals("./data/2-week/server_log.csv", s.svrPath);
        model.campaigns().removeSnapshot(hash);
    }

    @Test
    @DisplayName("Snapshot of null campaign")
    public void nullSnapshotTest() {
        assertThrows(NullPointerException.class,
                () -> new CampaignSnapshot(null));
    }

    @Test
    @DisplayName("Attempt to toggle filter")
    public void toggleFilterTest() {
        var hash = model.campaigns().snapshotCampaign();
        var s = model.campaigns().getSnapshot(hash);
        assertThrows(RuntimeException.class, () -> s.toggleFilter(1));
        model.campaigns().removeSnapshot(hash);
    }

    @Test
    @DisplayName("Attempt to toggle all filters")
    public void toggleAllFilters() {
        var hash = model.campaigns().snapshotCampaign();
        var s = model.campaigns().getSnapshot(hash);
        assertThrows(RuntimeException.class, () -> s.toggleAllFilters(true));
        model.campaigns().removeSnapshot(hash);
    }
}
