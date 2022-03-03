package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Bundle;
import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;

public class TotalCost implements Metric {

    

    @Override
    public Function<Bundle, ?> overall() {
        return bundle -> {
            switch (bundle.type()) {
                case IMPRESSION:
                    return (double) Math.round(1000 * bundle.stream()
                            .parallel()
                            .reduce(0.0, (acc, elem) -> acc + ((Impression)elem).impressionCost(), Double::sum)) / 1000;
                case CLICK:
                    return (double) Math.round(1000 * bundle.stream()
                            .parallel()
                            .reduce(0.0, (acc, elem) -> acc + ((Click)elem).clickCost(), Double::sum)) / 1000;
                default:
                    throw new IllegalArgumentException("File type IMPRESSION or CLICK expected");
            }
        };
    }
}
