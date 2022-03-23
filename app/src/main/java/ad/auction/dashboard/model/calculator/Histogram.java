package ad.auction.dashboard.model.calculator;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.function.Function;

public interface Histogram {

    public Function<Campaign, ArrayList<Point2D>> histogram();
}
