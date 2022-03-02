package ad.auction.dashboard.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.TestUtility;
import ad.auction.dashboard.model.files.FileTracker;
import ad.auction.dashboard.model.files.FileTracker.FileTrackerQuery;


public class MetricTest {

    private static final FileTracker ft = new FileTracker();

    @SuppressWarnings("unchecked")
    private static Stream<Object> getData(String filename) {
        String file = TestUtility.getResourceFile(filename);
        ft.query(FileTrackerQuery.TRACK, file);
        return (Stream<Object>)ft.query(FileTrackerQuery.READ, file).get();
    }

    @Test
    @DisplayName("Impression count")
    @Tag("model/calculator")
    public void impressionCountTest() {
        var data = getData("/data/2-week/server_log_1.csv");

        long result = (long)Metric.IMPRESSION_COUNT.calculate(data);
        assertEquals(46, result);
    }

    @Test
    @DisplayName("Click count")
    @Tag("model/calculator")
    public void clickCountTest() {
        var data = getData("/data/2-week/click_log.csv");

        long result = (long)Metric.CLICK_COUNT.calculate(data);
        assertEquals(23923, result);
    }

    @Test
    @DisplayName("Unqiues count")
    @Tag("model/calculator")
    public void uniquesCountTest() {
        var data = getData("/data/2-week/server_log_1.csv");

        long result = (long)Metric.UNIQUES_COUNT.calculate(data);
        assertEquals(45, result);
    }

    @Test
    @DisplayName("Bounces count")
    @Tag("model/calculator")
    public void bouncesCountTest() {
        var data = getData("/data/2-week/server_log.csv");
        
        long result = (long)Metric.BOUNCES_COUNT.calculate(data);
        assertEquals(8665, result);
    }

    @Test
    @DisplayName("Conversions count")
    @Tag("model/calculator")
    public void conversionsCountTest() {
        var data = getData("/data/2-week/server_log.csv");

        long result = (long)Metric.CONVERSIONS_COUNT.calculate(data);
        assertEquals(2026, result);
    }
}
