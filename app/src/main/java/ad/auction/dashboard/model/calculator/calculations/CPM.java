package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class CPM extends Metric {

    public CPM() {
        super("Cost per 1000 Impressions");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
        return c -> {
            long impressionCount = (long)Metrics.IMPRESSION_COUNT.getMetric().overall().apply(c)/1000;
            double totalCost = (double)Metrics.TOTAL_COST.getMetric().overall().apply(c);

            return Utility.roundNDp(totalCost/impressionCount, 5);
        };
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            ArrayList<Point2D> totalCost = Metrics.TOTAL_COST.getMetric().overTime(resolution).apply(c);
            ArrayList<Point2D> impressionCount = Metrics.IMPRESSION_COUNT.getMetric().overTime(resolution).apply(c);
            
            for (int i=0; i<totalCost.size(); i++) {
                var tcst = totalCost.get(i);
                points.add(new Point2D(tcst.getX(), (double)tcst.getY()/(impressionCount.get(i).getY()/1000)));
            }

            return points;
        };
    }
}
