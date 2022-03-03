package ad.auction.dashboard.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.TestUtility;
import ad.auction.dashboard.model.calculator.calculations.UniquesCount;
import ad.auction.dashboard.model.files.FileTracker;
import ad.auction.dashboard.model.files.FileType;
import ad.auction.dashboard.model.files.FileTracker.FileTrackerQuery;
import ad.auction.dashboard.model.files.records.Campaign;
import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;

public class MetricTest {

    private static final FileTracker ft = new FileTracker();

    private static Campaign c;

    @BeforeAll
    @SuppressWarnings("unchecked")
    public static void setup() {
        c = new Campaign(
            (List<Impression>)getData("/data/2-week/impression_log.csv"),
            (List<Click>)getData("/data/2-week/click_log.csv"),
            (List<Server>)getData("/data/2-week/server_log.csv"));
    }

    private static List<?> getData(String filename) {
        String file = TestUtility.getResourceFile(filename);
        ft.query(FileTrackerQuery.TRACK, file);
        return (List<?>) ft.query(FileTrackerQuery.READ, file).get();
    }

    @Test
    @DisplayName("Impression count")
    @Tag("model/calculator")
    public void impressionCountTest() {
        long result = (long) Metrics.IMPRESSION_COUNT.getMetric().overall().apply(c);
        assertEquals(486104, result);
    }

    @Test
    @DisplayName("Click count")
    @Tag("model/calculator")
    public void clickCountTest() {
        long result = (long) Metrics.CLICK_COUNT.getMetric().overall().apply(c);
        assertEquals(23923, result);
    }

    @Test
    @DisplayName("Unqiues count")
    @Tag("model/calculator")
    @Deprecated
    public void uniquesCountTest() {
        long result = (long) ((UniquesCount)Metrics.UNIQUES_COUNT.getMetric()).overall(FileType.IMPRESSION).apply(c);
        assertEquals(439832, result);
    }

    @Test
    @DisplayName("Bounces count")
    @Tag("model/calculator")
    public void bouncesCountTest() {
        long result = (long) Metrics.BOUNCES_COUNT.getMetric().overall().apply(c);
        assertEquals(8665, result);
    }

    @Test
    @DisplayName("Conversions count")
    @Tag("model/calculator")
    public void conversionsCountTest() {
        long result = (long) Metrics.CONVERSIONS_COUNT.getMetric().overall().apply(c);
        assertEquals(2026, result);
    }

    @Test
    @DisplayName("Total cost - Impression")
    @Tag("model/calculator")
    public void totalCostImpressionTest() {
        double result = (double) Metrics.TOTAL_COST_IMPRESSION.getMetric().overall().apply(c);
        assertEquals(487.055, result);
    }

    @Test
    @DisplayName("Total cost - Click")
    @Tag("model/calculator")
    public void totalCostClickTest() {
        double result = (double) Metrics.TOTAL_COST_CLICK.getMetric().overall().apply(c);
        assertEquals(117610.866, result);

    }

}
