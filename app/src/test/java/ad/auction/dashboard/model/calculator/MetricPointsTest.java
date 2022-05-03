package ad.auction.dashboard.model.calculator;

import ad.auction.dashboard.TestUtility;
import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.campaigns.Campaign;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class MetricPointsTest {

    private static Model model = new Model(false);

    private static Campaign c;

    @BeforeAll
    public static void setUp() {
        model.campaigns().newCampaign("points test",
                TestUtility.getResourceFile("/data/log_files/points_tests/clicks.csv"),
                TestUtility.getResourceFile("/data/log_files/points_tests/impressions.csv"),
                TestUtility.getResourceFile("/data/log_files/points_tests/server.csv"));
        model.campaigns().openCampaign("points test");
        c = model.campaigns().getCurrentCampaign();
    }

    @AfterAll
    public static void tearDown() {
        model.campaigns().removeCampaign("points test");
        model.close();
        model = null;
    }

    //FACTOR TEST


    //IMPRESSION COUNT

    @Test
    @DisplayName("Impression count points - cumulative")
    public void impressionCountPointsTestCumulative() {
        var res = Metrics.IMPRESSION_COUNT
                .getMetric()
                .overTime(ChronoUnit.DAYS, true, 1)
                .apply(c);

        assertEquals(9, res.size());

        for (int i=0; i<9; i++)
            assertEquals(i, res.get(i).getY());
    }

    @Test
    @DisplayName("Impression count points - trend")
    public void impressionCountPointsTrendTest() {
        var res = Metrics.IMPRESSION_COUNT
                .getMetric()
                .overTime(ChronoUnit.DAYS, false, 1)
                .apply(c);

        assertEquals(9, res.size());

        for (int i=1; i<9; i++)
            assertEquals(1, res.get(i).getY());
    }
}
