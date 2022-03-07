package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;
import javafx.geometry.Point2D;

public class BouncesCount implements Metric {

    public static final long BOUNCE_MAX_TIME_SEC = 60;

    @Override
    public Function<Campaign, Object> overall() {
        return c -> c.server()
                .filter(e -> e.pagesViewed() == 1 || e.secondsOnWebsite() < BOUNCE_MAX_TIME_SEC)
                .count();
    }

    @Override
    public Function<Campaign, HashSet<Point2D>> overTime(ChronoUnit timeResolution) {
        // TODO Auto-generated method stub
        return null;
    }
}
