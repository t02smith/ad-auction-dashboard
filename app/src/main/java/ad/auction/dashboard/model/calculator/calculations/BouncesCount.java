package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Campaign;

public class BouncesCount implements Metric {

    public static final long BOUNCE_MAX_TIME_SEC = 60;

    @Override
    public Function<Campaign, ?> overall() {
        return c -> c.server()
                .filter(e -> e.pagesViewed() == 1 || e.secondsOnWebsite() < BOUNCE_MAX_TIME_SEC)
                .count();
    }
}
