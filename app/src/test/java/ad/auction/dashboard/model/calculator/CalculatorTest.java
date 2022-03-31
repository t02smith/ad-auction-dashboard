package ad.auction.dashboard.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.*;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;


@Tag("model/calculator")
public class CalculatorTest {

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

    /*CALCULATIONS*/
    
    @Test
    @DisplayName("Run a single calculation")
    public void singleCalculationTest() throws Exception {
        Future<Object> res = model.runCalculation(Metrics.CPA, MetricFunction.OVERALL).get("2 week - test");

        while (!res.isDone()) {}

        assertEquals(58.291, (double)res.get());
    }

    @Test
    @DisplayName("Run a calculation many times")
    public void manyCalculationTest() {
        final HashSet<Future<Object>> calc = new HashSet<>();
        model.campaigns().getCurrentCampaign().setCacheValues(false);

        for (int i=0; i<10; i++) {
            calc.add(model.runCalculation(Metrics.CPA, MetricFunction.OVERALL).get("2 week - test"));
        }

        final HashSet<Future<Object>> correct = new HashSet<>();
        while (correct.size() != calc.size()) {
            calc.forEach(c -> {
                if (c.isDone() && !correct.contains(c)) {
                    try {
                        if ((double)c.get() == 58.291) correct.add(c);
                        else fail("Incorrect value");
                    } catch (Exception e) {fail("Error retrieving calculation result");}
                }
            });
        }
    }

}
