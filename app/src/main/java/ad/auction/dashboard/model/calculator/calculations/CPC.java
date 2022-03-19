package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class CPC extends Metric {

    public CPC() {
        super("Cost per Click");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
        return c -> {
            double totalCost = (double)Metrics.TOTAL_COST.getMetric().overall().apply(c);
            double clickCount = (long)Metrics.CLICK_COUNT.getMetric().overall().apply(c);
            return Utility.roundNDp(totalCost/clickCount,3);
        };
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            Future<ArrayList<Point2D>> totalCost = executor.submit(() -> Metrics.TOTAL_COST.getMetric().overTime(resolution).apply(c));
            Future<ArrayList<Point2D>> clickCount = executor.submit(() -> Metrics.CLICK_COUNT.getMetric().overTime(resolution).apply(c));
            while (!totalCost.isDone() || !clickCount.isDone()) {}

            try {
                for (int i = 1; i < totalCost.get().size(); i++) {
                    var tcst = totalCost.get().get(i);
                    points.add(new Point2D(tcst.getX(), (double) tcst.getY() / clickCount.get().get(i).getY()));
                }
            } catch (Exception ignored) {}

            return points;
        };
    }
}
