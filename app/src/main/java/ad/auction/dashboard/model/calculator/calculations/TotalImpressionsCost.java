package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;
import javafx.geometry.Point2D;

public class TotalImpressionsCost implements Metric {

    @Override
    public Function<Campaign, Object> overall() {
        return c -> (double) Math.round(1000 * c.impressions()
                .parallel()
                .reduce(0.0, (acc, elem) -> acc + elem.impressionCost(), Double::sum)) / 1000;

    };

    @Override
    public Function<Campaign, HashSet<Point2D>> overTime(ChronoUnit timeResolution) {
        // TODO Auto-generated method stub
        return null;
    }
}
