package ad.auction.dashboard.model.calculator;

import ad.auction.dashboard.model.calculator.calculations.*;

/**
 * A reference point for all different metrics
 * 
 * @author tcs1g20
 */
public enum Metrics {

    // Number of impressions
    IMPRESSION_COUNT(new ImpressionCount()),

    // Number of clicks
    CLICK_COUNT(new ClickCount()),

    // Number of records with unique ids
    UNIQUES_COUNT(new UniquesCount()),

    // Number of bounces (viewed 1 page or less than 60secs on website)
    BOUNCES_COUNT(new BouncesCount()),

    // Number of conversions
    CONVERSIONS_COUNT(new ConversionsCount()),

    // Total cost (impressions + clicks)
    TOTAL_COST_IMPRESSION(new TotalImpressionsCost()),
    TOTAL_COST_CLICK(new TotalClicksCost()),
    TOTAL_COST(new TotalCost()),

    // Click-through-rate - avg clicks per impression
    CTR(new CTR()),

    //Cost-per-acquisition - avg money spent for each conversion
    CPA(new CPA()),

    //Cost-per-click - avg money spent for each click
    CPC(new CPC()),

    //Cost-per-1000-impression
    CPM(new CPM()),

    BOUNCE_RATE(new BounceRate());

    //Produces a metric object to perform calculations
    private final Metric metric;

    private Metrics(Metric metric) {
        this.metric = metric;
    }

    public Metric getMetric() {
        return this.metric;
    }

}
