package ad.auction.dashboard.model.calculator;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

import java.util.List;
import java.util.function.Function;

/**
 * For metrics where a histogram can be drawn from it
 */
public interface Histogram {

    int DISTRIBUTION_GROUPS = 15;

    /**
     * Calculates the points for a histogram
     * @return (campaign name, chart points)
     */
    Function<Campaign, List<Point2D>> histogram();
}
