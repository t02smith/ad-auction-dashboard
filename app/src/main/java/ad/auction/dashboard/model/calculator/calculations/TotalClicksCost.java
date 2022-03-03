package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Campaign;

public class TotalClicksCost implements Metric {

    @Override
    public Function<Campaign, Object> overall() {
        return c -> (double) Math.round(1000 * c.clicks()
                .parallel()
                .reduce(0.0, (acc, elem) -> acc + elem.clickCost(), Double::sum)) / 1000;
    }
}
