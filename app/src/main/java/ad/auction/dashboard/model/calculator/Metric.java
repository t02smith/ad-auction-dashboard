package ad.auction.dashboard.model.calculator;

import java.util.function.Function;
import java.util.stream.Stream;

import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;

//TODO Finish metric functions

public enum Metric {
    
    IMPRESSION_COUNT        (stream -> stream.count()),
    CLICK_COUNT             (stream -> stream.count()),
    UNIQUES_COUNT           (stream -> stream.map(elem -> {
                                if (elem instanceof Impression) return ((Impression)elem).ID();
                                else if (elem instanceof Click) return ((Click)elem).ID();
                                else return ((Server)elem).ID();
                            }).distinct().count()),
    BOUNCES_COUNT           (stream -> stream.filter(e -> ((Server)e).pagesViewed() == 1).count()),
    CONVERSIONS_COUNT       (stream -> stream.filter(e -> ((Server)e).conversion()).count()),
    TOTAL_COST              (stream -> null),
    CTR                     (stream -> null),
    CPA                     (stream -> null),
    CPC                     (stream -> null),
    CPM                     (stream -> null),
    BOUNCE_RATE             (stream -> null);

    private Function<Stream<Object>, ?> calculator;

    private Metric(Function<Stream<Object>, ?> calculator) {
        this.calculator = calculator;
    }

    public Object calculate(Stream<Object> stream) {
        return this.calculator.apply(stream);
    } 


}
