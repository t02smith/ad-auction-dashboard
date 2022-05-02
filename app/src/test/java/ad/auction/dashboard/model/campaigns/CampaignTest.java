package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.TestUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CampaignTest {

    private Campaign campaign;

    @BeforeEach
    public void setUp() {
        campaign = new Campaign(
                "test campaign",
                TestUtility.getResourceFile("/data/log_files/2-week/click_log.csv"),
                TestUtility.getResourceFile("/data/log_files/2-week/impression_log.csv"),
                TestUtility.getResourceFile("/data/log_files/2-week/server_log.csv"));
    }

    @AfterEach
    public void tearDown() {
        campaign = null;
    }

    //GETTERS

    @Test
    @DisplayName("imp path getter")
    public void impPathTest() {
        assertEquals(
                TestUtility.getResourceFile("/data/log_files/2-week/impression_log.csv"),
                campaign.impPath()
        );
    }

    @Test
    @DisplayName("clk path getter")
    public void clkPathTest() {
        assertEquals(
                TestUtility.getResourceFile("/data/log_files/2-week/click_log.csv"),
                campaign.clkPath()
        );
    }

    @Test
    @DisplayName("svr path getter")
    public void svrPathTest() {
        assertEquals(
                TestUtility.getResourceFile("/data/log_files/2-week/server_log.csv"),
                campaign.svrPath()
        );
    }

    @Test
    @DisplayName("imp stream - not loaded")
    public void impStreamNotLoadedTest() {
        assertThrows(NullPointerException.class, () -> campaign.impressions());
    }

    @Test
    @DisplayName("clk stream - not loaded")
    public void clkStreamNotLoadedTest() {
        assertThrows(NullPointerException.class, () -> campaign.clicks());
    }

    @Test
    @DisplayName("svr stream - not loaded")
    public void svrStreamNotLoadedTest() {
        assertThrows(NullPointerException.class, () -> campaign.server());
    }
}
