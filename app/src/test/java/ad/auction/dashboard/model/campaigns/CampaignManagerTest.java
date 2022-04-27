package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.model.Model;
import org.junit.jupiter.api.*;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class CampaignManagerTest {

    private static final Model model = new Model();

    @BeforeEach
    public void setUp() {
        model.campaigns().newCampaign("2 week - test",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");
    }

    @AfterEach
    public void tearDown() {
        if (model.campaigns().campaigns.containsKey("2 week - test")) {
            if (model.campaigns().currentCampaign != null) model.campaigns().closeCurrentCampaign();
            model.campaigns().removeCampaign("2 week - test");
        }

    }

    @AfterAll
    public static void tearDownAll() {
        model.campaigns().closeAll();
    }

    /*OPEN CAMPAIGN*/

    @Test
    @DisplayName("Open campaign successfully")
    public void openCampaignTest() {
        var c = model.campaigns().getCampaignData("2 week - test");
        model.campaigns().openCampaign("2 week - test");
        assertEquals(c, model.campaigns().getCurrentCampaign().getData());

    }

    @Test
    @DisplayName("Open campaign twice")
    public void openCampaignTwiceTest() {
        model.campaigns().openCampaign("2 week - test");
        assertThrows(IllegalArgumentException.class, () -> model.campaigns().openCampaign("2 week - test"));
    }

    @Test
    @DisplayName("Open invalid campaign")
    public void openInvalidCampaign() {
        assertThrows(NoSuchElementException.class,
                () -> model.campaigns().openCampaign("fake campaign"));
    }

    /*NEW CAMPAIGN*/

    @Test
    @DisplayName("New campaign")
    public void createCampaignTest() {
        var c = model.campaigns().getCampaignData("2 week - test");
        assertEquals("2 week - test", c.name());
        assertEquals("./data/2-week/click_log.csv", c.clkPath());
        assertEquals("./data/2-week/impression_log.csv", c.impPath());
        assertEquals("./data/2-week/server_log.csv", c.svrPath());
    }

    @Test
    @DisplayName("New campaign - duplicate name")
    public void createCampaignWithDuplicateNameTest() {
        assertThrows(IllegalArgumentException.class,() -> model.campaigns().newCampaign("2 week - test", "", "", ""));
    }

    @Test
    @DisplayName("New campaign - invalid file types")
    public void createCampaignWithInvalidFiles() {
        var res = model.campaigns().newCampaign("invalid files campaign",
                "./data/2-week/impression_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/click_log.csv");

        assertFalse(res[0], "Expected invalid impressions file");
        assertTrue(res[1], "Expected valid clicks file");
        assertFalse(res[2], "Expected invalid server file");
    }

    /*EDIT CAMPAIGN*/

    @Test
    @DisplayName("Edit campaign")
    public void editCampaignTest() {
        model.campaigns().editCampaign("2 week - test", "new test name", "", "", "");

        assertEquals("new test name", model.campaigns().getCampaignData("new test name").name());
        model.campaigns().editCampaign("new test name", "2 week - test", "", "", "");

    }

    @Test
    @DisplayName("Edit campaign - duplicate name")
    public void editCampaignWithDuplicateNameTest() {
        model.campaigns().newCampaign("dupe name test",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");
        assertThrows(IllegalArgumentException.class, () -> model.campaigns().editCampaign("dupe name test", "2 week - test", null, null, null));
        model.campaigns().removeCampaign("dupe name test");
    }

    @Test
    @DisplayName("Edit campaign - campaign doesnt exist")
    public void editInvalidCampaignTest() {
        assertThrows(NoSuchElementException.class,
                () -> model.campaigns().editCampaign("fake campaign", "new name", null, null, null));
    }

    /*REMOVE campaign*/

    @Test
    @DisplayName("Remove campaign")
    public void removeCampaignTest() {
        model.campaigns().removeCampaign("2 week - test");
        assertFalse(model.campaigns().campaigns.containsKey("2 week - test"));
    }

    @Test
    @DisplayName("Remove invalid campaign")
    public void removeInvalidCampaignTest() {
        assertThrows(NoSuchElementException.class, () -> model.campaigns().removeCampaign("a fake campaign"));
    }

    /*LOAD CAMPAIGN*/

    @Test
    @DisplayName("Load invalid campaign")
    public void loadInvalidCampaignTest() {
        assertThrows(NoSuchElementException.class,() -> model.campaigns().loadCampaignData("not a real campaign"));
    }
}
