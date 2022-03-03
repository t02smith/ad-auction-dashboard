package ad.auction.dashboard.model.calculator;

import java.util.function.Supplier;

import ad.auction.dashboard.model.calculator.calculations.*;


//TODO Finish metric functions

/**
 * A reference point for all different metrics
 * 
 * @author tcs1g20
 */
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

    // Total cost (impressions)
    TOTAL_COST_IMPRESSION(() -> new TotalImpressionsCost()),

    TOTAL_COST_CLICK(() -> new TotalClicksCost()),

    CTR(() -> null),
    CPA(() -> null),
    CPC(() -> null),
    CPM(() -> null),
    BOUNCE_RATE(() -> null);

    //Produces a metric object to perform calculations
    private final Supplier<Metric> metric;

    
    private Metrics(Supplier<Metric> metric) {
        this.metric = metric;
    }

    public Metric getMetric() {
        return this.metric.get();
    }

}
