package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Bundle;

public class UniquesCount implements Metric {

    @Override
    public Function<Bundle, ?> overall() {
        return bundle -> bundle.stream()
                .map(elem -> elem.ID())
                .distinct()
                .count();
    }
}
