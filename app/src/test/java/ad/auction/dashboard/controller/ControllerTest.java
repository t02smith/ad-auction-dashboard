package ad.auction.dashboard.controller;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@Tag("controller")
public class ControllerTest {

    private Controller controller;

    @BeforeEach
    public void setUp() {
        this.controller = new Controller();
    }

    public void loadCampaigns() {
        var resA = controller.newCampaign("campaign A",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");
        var resB = controller.newCampaign("campaign B",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");

        while (!resA.isDone() || !resB.isDone());
    }

    @AfterEach
    public void tearDown() {
        this.controller = null;
    }

    // TESTS //

    @Test
    @DisplayName("Access controller after close")
    public void accessControllerAfterCloseTest() {
        this.controller.close();
        assertThrows(RuntimeException.class, () -> this.controller.isAvailable());
    }

    @Test
    @DisplayName("Toggle campaign on test")
    public void toggleCampaignOnTest() {
        this.loadCampaigns();
        var openA = controller.openCampaign("campaign A");
        while (!openA.isDone());


    }
}
