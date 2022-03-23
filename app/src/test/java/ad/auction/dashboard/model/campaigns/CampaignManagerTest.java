package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.model.Model;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CampaignManagerTest {

    private static final Model model = new Model();

    @BeforeEach
    public void setUp() {
        model.campaigns().newCampaign("2 week - test", "./data/click_log.csv", "./data/impression_log.csv", "./data/server_log.csv");
    }

    @AfterEach
    public void tearDown() {
        model.campaigns().removeCampaign("2 week - test");
    }

    @Test
    @DisplayName("New campaign")
    public void createCampaignTest() {
        var c = model.campaigns().getCampaignData("2 week - test");
        assertEquals("2 week - test", c.name());
        assertEquals("./data/click_log.csv", c.clkPath());
        assertEquals("./data/impression_log.csv", c.impPath());
        assertEquals("./data/server_log.csv", c.svrPath());
    }

    @Test
    @DisplayName("Edit campaign")
    public void editCampaignTest() {
        model.campaigns().editCampaign("2 week - test", "new test name", "", "", "");

        assertEquals("new test name", model.campaigns().getCampaignData("new test name").name());
        model.campaigns().editCampaign("2 week - test", "2 week - test", "", "", "");

    }

    @Test
    @DisplayName("Remove campaign")
    public void removeCampaignTest() {
        model.campaigns().removeCampaign("2 week - test");
        assertNull(model.campaigns().getCampaignData("2 week - test"));
    }
}
