package ad.auction.dashboard.model.calculator;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.function.Function;

/**
 * For metrics where a histogram can be drawn from it
 */
public interface Histogram {

    public Function<Campaign, List<Point2D>> histogram();
}
