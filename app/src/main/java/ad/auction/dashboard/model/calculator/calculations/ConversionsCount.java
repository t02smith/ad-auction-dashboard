package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Bundle;
import ad.auction.dashboard.model.files.records.Server;

public class ConversionsCount implements Metric {

    @Override
    public Function<Bundle, ?> overall() {
        return bundle -> bundle.stream()
                .filter(e -> ((Server)e).conversion())
                .count();
    }
}
