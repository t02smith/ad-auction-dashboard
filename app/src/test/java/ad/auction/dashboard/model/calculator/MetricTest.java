package ad.auction.dashboard.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.TestUtility;
import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.Campaigns.CampaignManager.CMQuery;
import ad.auction.dashboard.model.calculator.calculations.UniquesCount;
import javafx.geometry.Point2D;


@Tag("model/calculator")
public class MetricTest {

    private static final Model model = new Model();

    private static Campaign c;

    @BeforeAll
    public static void setup() {

        // model.queryCampaignManager(
        //     CMQuery.NEW_CAMPAIGN, 
        //     "campaign 1",
        //     "./data/impression_log.csv",
        //     "./data/click_log.csv",
        //     "./data/server_log.csv");
        
        model.queryCampaignManager(CMQuery.OPEN_CAMPAIGN, "Campaign 1");
        c = (Campaign)model.queryCampaignManager(CMQuery.GET_CAMPAIGN, "Campaign 1").get();

    }

    @Test
    public void overTimeTest() {
        ArrayList<Point2D> actual = Metrics.UNIQUES_COUNT.getMetric().overTime(ChronoUnit.DAYS).apply(c);

        var it = actual.iterator();
        while (it.hasNext()) {
            System.out.println(it.next().toString());
        }

    }


    // OVERALL

    @Test
    @DisplayName("Impression count")
    public void impressionCountTest() {
        long result = (long) Metrics.IMPRESSION_COUNT.getMetric().overall().apply(c);
        assertEquals(486104, result);
    }

    @Test
    @DisplayName("Click count")
    public void clickCountTest() {
        long result = (long) Metrics.CLICK_COUNT.getMetric().overall().apply(c);
        assertEquals(23923, result);
    }

    @Test
    @DisplayName("Unqiues count")
    @Deprecated
    public void uniquesCountTest() {
        long result = (long) ((UniquesCount) Metrics.UNIQUES_COUNT.getMetric()).overall().apply(c);
        assertEquals(23806, result);
    }

    @Test
    @DisplayName("Bounces count")
    public void bouncesCountTest() {
        long result = (long) Metrics.BOUNCES_COUNT.getMetric().overall().apply(c);
        assertEquals(8665, result);
    }

    @Test
    @DisplayName("Conversions count")
    public void conversionsCountTest() {
        long result = (long) Metrics.CONVERSIONS_COUNT.getMetric().overall().apply(c);
        assertEquals(2026, result);
    }

    @Test
    @DisplayName("Total cost - Impression")
    public void totalCostImpressionTest() {
        double result = (double) Metrics.TOTAL_COST_IMPRESSION.getMetric().overall().apply(c);
        assertEquals(487.055, result);
    }

    @Test
    @DisplayName("Total cost - Click")
    public void totalCostClickTest() {
        double result = (double) Metrics.TOTAL_COST_CLICK.getMetric().overall().apply(c);
        assertEquals(117610.866, result);

    }

    @Test
    @DisplayName("CTR")
    public void ctrTest() {
        double result = (double) Metrics.CTR.getMetric().overall().apply(c);
        assertEquals(0.04921, result);
    }

    @Test
    @DisplayName("CPA")
    public void cpaTest() {
        double result = (double) Metrics.CPA.getMetric().overall().apply(c);
        assertEquals(58.291, result);
    }

    @Test
    @DisplayName("CPC")
    public void cpcTest() {
        double result = (double) Metrics.CPC.getMetric().overall().apply(c);
        assertEquals(4.937, result);
    }

    @Test
    @DisplayName("CPM")
    public void cpmTest() {
        double result = (double) Metrics.CPM.getMetric().overall().apply(c);
        assertEquals(242.99984, result);
    }

    @Test
    @DisplayName("Bounce Rate")
    public void bounceRateTest() {
        double result = (double) Metrics.BOUNCE_RATE.getMetric().overall().apply(c);
        assertEquals(0.362, result);
    }

}
