package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.calculator.Metrics;
import javafx.geometry.Point2D;

public class CPA implements Metric {

    @Override
    public Function<Campaign, Object> overall() {
        return c -> {
            double totalCost = (double)Metrics.TOTAL_COST.getMetric().overall().apply(c);
            long conversionNo = (long)Metrics.CONVERSIONS_COUNT.getMetric().overall().apply(c);
            
            return Utility.roundNDp(totalCost/conversionNo, 3);
        };
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            ArrayList<Point2D> totalCost = Metrics.TOTAL_COST.getMetric().overTime(resolution).apply(c);
            ArrayList<Point2D> conversionNo = Metrics.CONVERSIONS_COUNT.getMetric().overTime(resolution).apply(c);
            
            for (int i=0; i<totalCost.size(); i++) {
                var tcst = totalCost.get(i);
                points.add(new Point2D(tcst.getX(), (double)tcst.getY()/conversionNo.get(i).getY()));
            }

            return points;
        };
    }
}
