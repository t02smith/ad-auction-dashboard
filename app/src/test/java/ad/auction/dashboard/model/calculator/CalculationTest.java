package ad.auction.dashboard.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import ad.auction.dashboard.model.campaigns.Campaign;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import javafx.geometry.Point2D;

public class CalculationTest {

    private static final Model model = new Model();

    @BeforeAll
    public static void setUp() {
        model.campaigns().newCampaign("2 week - test",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");
        model.campaigns().openCampaign("2 week - test");
    }

    @AfterAll
    public static void tearDown() {
        model.campaigns().removeCampaign("2 week - test");
        model.close();
    }
    
    @Test
    @DisplayName("Run an overall calculation")
    public void runOverallTest() throws Exception {
        var actual = model.runCalculation(Metrics.TOTAL_COST, MetricFunction.OVERALL).get("2 week - test");
        while (!actual.isDone()) {}

        assertEquals(118097.921, (double)actual.get());
    }

    @Test
    @DisplayName("Run an over time calculation")
    @SuppressWarnings("unchecked")
    public void runOvertimeCorrectSizeTest() throws Exception {
        model.setCumulative(true);
        model.setTimeResolution(ChronoUnit.DAYS);
        var actual = model.runCalculation(Metrics.TOTAL_COST, MetricFunction.OVER_TIME).get("2 week - test");
        while (!actual.isDone()) {}

        assertEquals(14, ((ArrayList<Point2D>)actual.get()).size());
    }

    @Test
    @DisplayName("Run a random value calculation")
    @SuppressWarnings("unchecked")
    public void runOvertimeRandomValueTest() throws Exception {
        model.setFactor(1);
        var actual = model.runCalculation(Metrics.TOTAL_COST, MetricFunction.OVER_TIME).get("2 week - test");
        while (!actual.isDone()) {}

        assertEquals(50712.703487540944, ((ArrayList<Point2D>)actual.get()).get(6).getY());
    }

    /*ERRORS*/

    @Test
    @DisplayName("Null campaign")
    public void nullCampaignTest() {
        var nullCalc = new Calculation<Number>(c -> 5, null);
        assertThrows(NullPointerException.class, nullCalc::call);
    }

    @Test
    @DisplayName("Null function")
    public void nullFunctionTest() {
        var nullFunc = new Calculation<Number>(null, model.campaigns().getCurrentCampaign());
        assertThrows(NullPointerException.class, nullFunc::call);
    }

    @Test
    @DisplayName("Non loaded campaign")
    public void nonLoadedCampaign() {
        var camp = new Campaign("non-loaded", "imp", "clk", "svr");
        var calc = new Calculation<Number>(c -> 5, camp);
        assertThrows(IllegalStateException.class, calc::call);
    }

}
