package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.calculator.Metrics;

public class CPM implements Metric {
    
    @Override
    public Function<Campaign, Object> overall() {
        return c -> {
            long impressionCount = (long)Metrics.IMPRESSION_COUNT.getMetric().overall().apply(c)/1000;
            double totalCost = (double)Metrics.TOTAL_COST.getMetric().overall().apply(c);

            return Utility.roundNDp(totalCost/impressionCount, 5);
        };
    }
}
