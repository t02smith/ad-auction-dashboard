package ad.auction.dashboard.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import ad.auction.dashboard.model.calculator.calculations.Metric;
import org.junit.jupiter.api.*;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;
import org.junit.jupiter.params.ParameterizedTest;


@Tag("model/calculator")
public class MetricTest {

    private static final Model model = new Model();

    private static Campaign c;

    @BeforeAll
    public static void setUp() {
        model.campaigns().newCampaign("2 week - test",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");
        model.campaigns().openCampaign("2 week - test");
        c = model.campaigns().getCurrentCampaign();
        c.setCacheValues(false);
    }

    @AfterAll
    public static void tearDown() {
        model.campaigns().removeCampaign("2 week - test");
        model.close();
    }

    //

    @Test
    @DisplayName("Increment date - hours")
    public void incrementDateHoursTest() {
        var date = LocalDateTime.of(LocalDate.of(2002,1,10), LocalTime.NOON);
        var res = Metric.incrementDate(ChronoUnit.HOURS, date, 1);

        assertEquals(
                LocalDateTime.of(LocalDate.of(2002,1,10), LocalTime.of(13,0)),
                res
        );
    }

    @Test
    @DisplayName("Increment date - day")
    public void incrementDateDaysTest() {
        var date = LocalDateTime.of(LocalDate.of(2002,1,10), LocalTime.NOON);
        var res = Metric.incrementDate(ChronoUnit.DAYS, date, 6);

        assertEquals(
                LocalDateTime.of(LocalDate.of(2002,1,10), LocalTime.of(16,0)),
                res
        );
    }

    @Test
    @DisplayName("Increment date - week")
    public void incrementDateWeekTest() {
        var date = LocalDateTime.of(LocalDate.of(2002,1,10), LocalTime.NOON);
        var res = Metric.incrementDate(ChronoUnit.WEEKS, date, 2);

        assertEquals(
                LocalDateTime.of(LocalDate.of(2002,1,13), LocalTime.NOON),
                res
        );
    }

    @Test
    @DisplayName("Increment date - invalid time res")
    public void incrementDateInvalidRes() {
        assertThrows(IllegalArgumentException.class, () ->
                Metric.incrementDate(ChronoUnit.ERAS, LocalDateTime.MIN, 2));
    }

    @Test
    @DisplayName("Increment date - invalid factor")
    public void incrementDateInvalidFactor() {
        assertThrows(IllegalArgumentException.class, () ->
                Metric.incrementDate(ChronoUnit.DAYS, LocalDateTime.MIN, 0));
    }



    // OVERALL

    @RepeatedTest(5)
    @DisplayName("Impression count")
    public void impressionCountTest() {
        long result = (long) Metrics.IMPRESSION_COUNT.getMetric().overall().apply(c);
        assertEquals(486104, result);
    }

    @RepeatedTest(5)
    @DisplayName("Click count")
    public void clickCountTest() {
        long result = (long) Metrics.CLICK_COUNT.getMetric().overall().apply(c);
        assertEquals(23923, result);
    }

    @RepeatedTest(5)
    @DisplayName("Uniques count")
    @Deprecated
    public void uniquesCountTest() {
        long result = (long) Metrics.UNIQUES_COUNT.getMetric().overall().apply(c);
        assertEquals(23806, result);
    }

    @RepeatedTest(5)
    @DisplayName("Bounces count")
    public void bouncesCountTest() {
        long result = (long) Metrics.BOUNCES_COUNT.getMetric().overall().apply(c);
        assertEquals(8665, result);
    }

    @RepeatedTest(5)
    @DisplayName("Conversions count")
    public void conversionsCountTest() {
        long result = (long) Metrics.CONVERSIONS_COUNT.getMetric().overall().apply(c);
        assertEquals(2026, result);
    }

    @RepeatedTest(5)
    @DisplayName("Total cost - Impression")
    public void totalCostImpressionTest() {
        double result = (double) Metrics.TOTAL_COST_IMPRESSION.getMetric().overall().apply(c);
        assertEquals(487.055, result);
    }

    @RepeatedTest(5)
    @DisplayName("Total cost - Click")
    public void totalCostClickTest() {
        double result = (double) Metrics.TOTAL_COST_CLICK.getMetric().overall().apply(c);
        assertEquals(117610.866, result);

    }

    @RepeatedTest(5)
    @DisplayName("Total cost")
    public void totalCostTest() {
        double result = (double) Metrics.TOTAL_COST.getMetric().overall().apply(c);
        assertEquals(118097.921, result);

    }

    @RepeatedTest(5)
    @DisplayName("CTR")
    public void ctrTest() {
        double result = (double) Metrics.CTR.getMetric().overall().apply(c);
        assertEquals(0.04921, result);
    }

    @RepeatedTest(5)
    @DisplayName("CPA")
    public void cpaTest() {
        double result = (double) Metrics.CPA.getMetric().overall().apply(c);
        assertEquals(58.291, result);
    }

    @RepeatedTest(5)
    @DisplayName("CPC")
    public void cpcTest() {
        double result = (double) Metrics.CPC.getMetric().overall().apply(c);
        assertEquals(4.937, result);
    }

    @RepeatedTest(5)
    @DisplayName("CPM")
    public void cpmTest() {
        double result = (double) Metrics.CPM.getMetric().overall().apply(c);
        assertEquals(242.99984, result);
    }

    @RepeatedTest(5)
    @DisplayName("Bounce Rate")
    public void bounceRateTest() {
        double result = (double) Metrics.BOUNCE_RATE.getMetric().overall().apply(c);
        assertEquals(0.362, result);
    }

}
