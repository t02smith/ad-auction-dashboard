package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class BounceRate extends Metric {

    public BounceRate() {
        super("Bounce Rate", "bounces/click");
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
    public Function<Campaign, ArrayList<Point2D>> cumulative(ChronoUnit resolution, boolean isCumulative) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            Future<ArrayList<Point2D>> bouncesCount = executor.submit(() -> Metrics.BOUNCES_COUNT.getMetric().cumulative(resolution, isCumulative).apply(c));
            Future<ArrayList<Point2D>> clickPts = executor.submit(() -> Metrics.CLICK_COUNT.getMetric().cumulative(resolution, isCumulative).apply(c));

            while (!bouncesCount.isDone() || !clickPts.isDone()) {}

            try {
                for (int i=1; i<bouncesCount.get().size(); i++) {
                    var bounceP = bouncesCount.get().get(i);
                    points.add(new Point2D(bounceP.getX(), bounceP.dotProduct(0, (double)1/clickPts.get().get(i).getY())));
                }
            } catch (Exception ignored) {}


            return points;
        };
    }
}
