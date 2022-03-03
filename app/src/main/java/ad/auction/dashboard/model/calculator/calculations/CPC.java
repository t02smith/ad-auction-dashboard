package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.files.records.Campaign;

public class CPC implements Metric {
    
    @Override
    public Function<Campaign, Object> overall() {
        return c -> {
            double totalCost = (double)Metrics.TOTAL_COST.getMetric().overall().apply(c);
            double clickCount = (long)Metrics.CLICK_COUNT.getMetric().overall().apply(c);
            return Utility.roundNDp(totalCost/clickCount,3);
        };
    }
}
