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

public class CTR extends Metric {

    public CTR() {
        super("Click Through Rate",
                "clicks/impression",
                "The average number of clicks for every impression.");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
        return c -> {
            long clickCount = (long)Metrics.CLICK_COUNT.getMetric().overall().apply(c);
            long impressionCount = (long)Metrics.IMPRESSION_COUNT.getMetric().overall().apply(c);
            
            return Util.roundNDp(((double)clickCount/impressionCount), 5);
        };
    }

    @Override
    public Function<Campaign, List<Point2D>> overTime(ChronoUnit resolution, boolean isCumulative, int factor) {
        return c -> {           
            ArrayList<Point2D> points = new ArrayList<>();

            Future<List<Point2D>> impressionPts = executor.submit(() -> Metrics.IMPRESSION_COUNT.getMetric().overTime(resolution, isCumulative, factor).apply(c));
            Future<List<Point2D>> clickPts = executor.submit(() -> Metrics.CLICK_COUNT.getMetric().overTime(resolution, isCumulative, factor).apply(c));
            while (!impressionPts.isDone() || !clickPts.isDone()) {}

            try {
                for (int i = 1; i < impressionPts.get().size(); i++) {
                    var clk = clickPts.get().get(i);
                    points.add(new Point2D(clk.getX(), clk.getY() / impressionPts.get().get(i).getY()));
                }
            } catch (Exception ignored) {}

            return points;
        };
    }
}
