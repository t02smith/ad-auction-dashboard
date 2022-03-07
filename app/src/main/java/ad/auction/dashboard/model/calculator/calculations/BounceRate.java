package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.calculator.Metrics;
import javafx.geometry.Point2D;

public class BounceRate implements Metric {
    
    @Override
    public Function<Campaign, Object> overall() {
        return c -> {
            long bounceCount = (long)Metrics.BOUNCES_COUNT.getMetric().overall().apply(c);
            long clickCount = (long)Metrics.CLICK_COUNT.getMetric().overall().apply(c);

            return Utility.roundNDp((double)bounceCount/clickCount, 3);
        };
    }

    @Override
    public Function<Campaign, HashSet<Point2D>> overTime(ChronoUnit timeResolution) {
        // TODO Auto-generated method stub
        return null;
    }
}
