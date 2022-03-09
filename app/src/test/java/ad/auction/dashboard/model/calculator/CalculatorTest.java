package ad.auction.dashboard.model.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.CampaignManager.CMQuery;


@Tag("model/calculator")
public class CalculatorTest {

    private static Model model = new Model();


    @BeforeAll
    public static void setUp() {
        model.queryCampaignManager(
            CMQuery.NEW_CAMPAIGN, 
            "campaign 1",
            "./data/impression_log.csv",
            "./data/click_log.csv",
            "./data/server_log.csv");
        
        model.queryCampaignManager(CMQuery.OPEN_CAMPAIGN, "campaign 1");
    }
    
    @Test
    public void singleCalculationTest() throws Exception {
        Future<Object> res = model.runCalculation(Metrics.CPA, MetricFunction.OVERALL);

        while (!res.isDone()) {}

        assertEquals(58.291, (double)res.get());
    }

    @Test
    public void manyCalculationTest() throws Exception {
        ArrayList<Future<Object>> calcs = new ArrayList<>();
        HashSet<Future<Object>> correct = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            calcs.add(model.runCalculation(Metrics.CPA, MetricFunction.OVERALL));
        }

        while (correct.size() > 0) {
            for (Future<Object> f: calcs) {
                if (!f.isDone()) continue;

                if ((double)f.get() == 58.291) {
                    if (!correct.contains(f)) correct.add(f);
                } else fail();
                
            }
        }
    }
}
