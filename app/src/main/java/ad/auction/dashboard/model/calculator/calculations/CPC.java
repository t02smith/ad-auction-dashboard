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

public class CPC extends Metric {

    public CPC() {
        super("Cost per Click",
                "£/click",
                "The average amount of money spent on an ad campaign per click.");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
        return c -> {
            double totalCost = (double)Metrics.TOTAL_COST.getMetric().overall().apply(c);
            double clickCount = (long)Metrics.CLICK_COUNT.getMetric().overall().apply(c);
            return Util.roundNDp(totalCost/clickCount,3);
        };
    }

    @Override
    public Function<Campaign, List<Point2D>> overTime(ChronoUnit resolution, boolean isCumulative, int factor) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            Future<List<Point2D>> totalCost = executor.submit(() -> Metrics.TOTAL_COST.getMetric().overTime(resolution, isCumulative, factor).apply(c));
            Future<List<Point2D>> clickCount = executor.submit(() -> Metrics.CLICK_COUNT.getMetric().overTime(resolution, isCumulative, factor).apply(c));
            while (!totalCost.isDone() || !clickCount.isDone()) {}

            try {
                for (int i = 1; i < totalCost.get().size(); i++) {
                    var tcst = totalCost.get().get(i);
                    points.add(new Point2D(tcst.getX(), tcst.getY() / clickCount.get().get(i).getY()));
                }
            } catch (Exception ignored) {}

            return points;
        };
    }
}
