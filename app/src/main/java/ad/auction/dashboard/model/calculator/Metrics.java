package ad.auction.dashboard.model.calculator;

import java.util.function.Supplier;

import ad.auction.dashboard.model.calculator.calculations.*;


//TODO Finish metric functions

public enum Metrics {

    // Number of impressions
    IMPRESSION_COUNT(() -> new ImpressionCount()),

    // Number of clicks
    CLICK_COUNT(() -> new ClickCount()),

    // Number of records with unique ids
    UNIQUES_COUNT(() -> new UniquesCount()),

    // Number of bounces (viewed 1 page or less than 60secs on website)
    BOUNCES_COUNT(() -> new BouncesCount()),

    // Number of conversions
    CONVERSIONS_COUNT(() -> new ConversionsCount()),

    // Total cost (clicks or impressions)
    TOTAL_COST(() -> new TotalCost()),
    CTR(() -> null),
    CPA(() -> null),
    CPC(() -> null),
    CPM(() -> null),
    BOUNCE_RATE(() -> null);

    private final Supplier<Metric> metric;

    private Metrics(Supplier<Metric> metric) {
        this.metric = metric;
    }

    public Metric getMetric() {
        return this.metric.get();
    }

}
