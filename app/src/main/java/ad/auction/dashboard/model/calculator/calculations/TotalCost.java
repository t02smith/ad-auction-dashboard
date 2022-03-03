package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.files.records.Campaign;

public class TotalCost implements Metric {

    @Override
    public Function<Campaign, Object> overall() {
        return c -> (double) Metrics.TOTAL_COST_CLICK.getMetric().overall().apply(c) +
                (double) Metrics.TOTAL_COST_IMPRESSION.getMetric().overall().apply(c);

    }
}
