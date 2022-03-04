package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.calculator.Metrics;

public class CTR implements Metric {
    
    @Override
    public Function<Campaign, Object> overall() {
        return c -> {
            long clickCount = (long)Metrics.CLICK_COUNT.getMetric().overall().apply(c);
            long impressionCount = (long)Metrics.IMPRESSION_COUNT.getMetric().overall().apply(c);
            
            return Utility.roundNDp(((double)clickCount/impressionCount), 5);
        };
    }
}
