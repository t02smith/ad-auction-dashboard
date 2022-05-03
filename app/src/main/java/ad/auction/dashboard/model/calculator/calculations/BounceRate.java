package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import ad.auction.dashboard.model.Util;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class BounceRate extends Metric {

    public BounceRate() {
        super("Bounce Rate",
                "bounces/click",
                "The average number of bounces per click");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
        return c -> {
            long bounceCount = (long)Metrics.BOUNCES_COUNT.getMetric().overall().apply(c);
            long clickCount = (long)Metrics.CLICK_COUNT.getMetric().overall().apply(c);

            return Util.roundNDp((double)bounceCount/clickCount, 3);
        };
    }

    @Override
    public Function<Campaign, List<Point2D>> overTime(ChronoUnit resolution, boolean isCumulative, int factor) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            Future<List<Point2D>> bouncesCount = executor.submit(() -> Metrics.BOUNCES_COUNT.getMetric().overTime(resolution, isCumulative, factor).apply(c));
            Future<List<Point2D>> clickPts = executor.submit(() -> Metrics.CLICK_COUNT.getMetric().overTime(resolution, isCumulative, factor).apply(c));

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
