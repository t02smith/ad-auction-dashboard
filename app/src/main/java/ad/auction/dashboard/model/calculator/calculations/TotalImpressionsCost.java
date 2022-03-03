package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Campaign;

public class TotalImpressionsCost implements Metric {

    @Override
    public Function<Campaign, Object> overall() {
        return c -> (double) Math.round(1000 * c.impressions()
                .parallel()
                .reduce(0.0, (acc, elem) -> acc + elem.impressionCost(), Double::sum)) / 1000;

    };
}
