package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class BounceRate extends Metric {

    public BounceRate() {
        super("Bounce Rate");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
        return c -> {
            long bounceCount = (long)Metrics.BOUNCES_COUNT.getMetric().overall().apply(c);
            long clickCount = (long)Metrics.CLICK_COUNT.getMetric().overall().apply(c);

            return Utility.roundNDp((double)bounceCount/clickCount, 3);
        };
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            ArrayList<Point2D> bouncesCount = Metrics.BOUNCES_COUNT.getMetric().overTime(resolution).apply(c);
            ArrayList<Point2D> clickPts = Metrics.CLICK_COUNT.getMetric().overTime(resolution).apply(c);
            
            for (int i=1; i<bouncesCount.size(); i++) {
                var bounceP = bouncesCount.get(i);
                points.add(new Point2D(bounceP.getX(), bounceP.dotProduct(0, (double)1/clickPts.get(i).getY())));
            }

            return points;
        };
    }
}
