package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Bundle;
import ad.auction.dashboard.model.files.records.Server;

public class BouncesCount implements Metric {

    public static final long BOUNCE_MAX_TIME_SEC = 60;

    @Override
    public Function<Bundle, ?> overall() {
        return bundle -> bundle.stream()
                .map(e -> (Server) e)
                .filter(e -> e.pagesViewed() == 1 || e.secondsOnWebsite() < BOUNCE_MAX_TIME_SEC)
                .count();
    }
}
