package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class CPA extends Metric {

    public CPA() {
        super("Cost per Acquisition", "£/conversion");
    }

    @Override
    public Function<Campaign, Number> overall() {
        return c -> {
            double totalCost = (double)Metrics.TOTAL_COST.getMetric().overall().apply(c);
            long conversionNo = (long)Metrics.CONVERSIONS_COUNT.getMetric().overall().apply(c);
            
            return Utility.roundNDp(totalCost/conversionNo, 3);
        };
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> cumulative(ChronoUnit resolution, boolean isCumulative) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            Future<ArrayList<Point2D>> totalCost = executor.submit(() -> Metrics.TOTAL_COST.getMetric().cumulative(resolution, isCumulative).apply(c));
            Future<ArrayList<Point2D>> conversionNo = executor.submit(() -> Metrics.CONVERSIONS_COUNT.getMetric().cumulative(resolution, isCumulative).apply(c));
            while (!totalCost.isDone() || !conversionNo.isDone()) {}

            try {
                for (int i=1; i<totalCost.get().size(); i++) {
                    var tcst = totalCost.get().get(i);
                    points.add(new Point2D(tcst.getX(), tcst.getY() /conversionNo.get().get(i).getY()));
                }
            } catch (Exception ignored) { }


            return points;
        };
    }
}
