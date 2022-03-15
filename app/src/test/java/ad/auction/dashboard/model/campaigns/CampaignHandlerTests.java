package ad.auction.dashboard.model.campaigns;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("model/campaigns")
public class CampaignHandlerTests {

    public CampaignHandler handler;

    @BeforeEach
    public void setUp() {
        this.handler = new CampaignHandler();
    }
    
    @Test
    @DisplayName("read file")
    public void readFileTest() {
        this.handler.parse(
            this.getClass().getResource("/data/campaigns.xml").toExternalForm()
        );

        var actual = this.handler.getCampaigns();
        var campaign = actual.get(0);

        assertTrue(
            campaign.name().equals("Campaign 1") &&
            campaign.getData().impPath().equals("./data/impression_log.csv") &&
            campaign.getData().svrPath().equals("./data/server_log.csv") &&
            campaign.getData().clkPath().equals("./data/click_log.csv")
        );
    }

    @Test
    @DisplayName("Incorrect file location")
    public void incorrectFileTest() {
        this.handler.parse("fake/address");

        assertTrue(this.handler.getCampaigns().size() == 0);
    }
}
