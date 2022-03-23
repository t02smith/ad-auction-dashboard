package ad.auction.dashboard.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

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
        model.campaigns().newCampaign("2 week - test", "./data/click_log.csv", "./data/impression_log.csv", "./data/server_log.csv");
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
        var actual = model.runCalculation(Metrics.TOTAL_COST, MetricFunction.OVERALL);
        while (!actual.isDone()) {}

        assertEquals(118097.919, (double)actual.get());
    }

    @Test
    @DisplayName("Run an over time calculation")
    @SuppressWarnings("unchecked")
    public void runOvertimeCorrectSizeTest() throws Exception {
        var actual = model.runCalculation(Metrics.TOTAL_COST, MetricFunction.OVER_TIME);
        while (!actual.isDone()) {}

        assertEquals(14, ((ArrayList<Point2D>)actual.get()).size());
    }

    @Test
    @DisplayName("Run an over time calculation")
    @SuppressWarnings("unchecked")
    public void runOvertimeRandomValueTest() throws Exception {
        var actual = model.runCalculation(Metrics.TOTAL_COST, MetricFunction.OVER_TIME);
        while (!actual.isDone()) {}

        assertEquals(50799.194687536336, ((ArrayList<Point2D>)actual.get()).get(6).getY());
    }
}
