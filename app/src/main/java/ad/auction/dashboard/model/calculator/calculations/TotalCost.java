package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.calculator.Metrics;
import javafx.geometry.Point2D;

public class TotalCost implements Metric {

    @Override
    public Function<Campaign, Object> overall() {
        return c -> (double) Metrics.TOTAL_COST_CLICK.getMetric().overall().apply(c) +
                (double) Metrics.TOTAL_COST_IMPRESSION.getMetric().overall().apply(c);

    }

    @Override
    public Function<Campaign, HashSet<Point2D>> overTime(ChronoUnit timeResolution) {
        // TODO Auto-generated method stub
        return null;
    }
}
