package ad.auction.dashboard.model.calculator.calculations;

import java.util.HashSet;
import java.util.function.BiFunction;
import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;
import javafx.geometry.Point2D;


public class ImpressionCount implements Metric {
    
    @Override
    public Function<Campaign, Object> overall() {
        return c -> c.impressions().count();
    }

    // @Override
    // public BiFunction<Campaign, TimeResolution, HashSet<Point2D>> overTime() {
    //     return (c, t) -> {
            


    //         return null;
    //     };
    // }
}
