package ad.auction.dashboard.model.calculator;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Bundle;
import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;

//TODO Finish metric functions

public enum Metric {

    // Number of impressions
    IMPRESSION_COUNT(bundle -> bundle.stream().count()),

    // Number of clicks
    CLICK_COUNT(bundle -> bundle.stream().count()),

    // Number of records with unique ids
    UNIQUES_COUNT(bundle -> bundle.stream()
            .map(elem -> elem.ID())
            .distinct()
            .count()),

    // Number of bounces (viewed 1 page or less than 60secs on website)
    BOUNCES_COUNT(bundle -> bundle.stream()
            .map(e -> (Server) e)
            .filter(e -> e.pagesViewed() == 1 || e.secondsOnWebsite() < 60)
            .count()),

    // Number of conversions
    CONVERSIONS_COUNT(bundle -> bundle.stream()
            .filter(e -> ((Server) e).conversion())
            .count()),

    // Total cost (clicks or impressions)
    TOTAL_COST(bundle -> {
        switch (bundle.type()) {
            case IMPRESSION:
                return (double) Math.round(1000 * bundle.stream()
                        .parallel()
                        .reduce(0.0, (acc, elem) -> acc + ((Impression) elem).impressionCost(), Double::sum)) / 1000;
            case CLICK:
                return (double) Math.round(1000 * bundle.stream()
                        .parallel()
                        .reduce(0.0, (acc, elem) -> acc + ((Click) elem).clickCost(), Double::sum)) / 1000;
            default:
                throw new IllegalArgumentException("File type IMPRESSION or CLICK expected");
        }
    }),
    CTR(bundle -> null),
    CPA(bundle -> null),
    CPC(bundle -> null),
    CPM(bundle -> null),
    BOUNCE_RATE(bundle -> null);

    private Function<Bundle, ?> calculator;

    private Metric(Function<Bundle, ?> calculator) {
        this.calculator = calculator;
    }

    public Object calculate(Bundle bundle) {
        return this.calculator.apply(bundle);
    }

}
