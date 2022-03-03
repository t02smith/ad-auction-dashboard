package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Bundle;

public class ImpressionCount implements Metric {
    
    @Override
    public Function<Bundle, ?> overall() {
        return (bundle) -> bundle.stream().count();
    }
}
