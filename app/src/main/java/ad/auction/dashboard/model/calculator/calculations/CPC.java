package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    public Function<Campaign, Object> overall() {
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

            ArrayList<Point2D> totalCost = Metrics.TOTAL_COST.getMetric().overTime(resolution).apply(c);
            ArrayList<Point2D> clickCount = Metrics.CLICK_COUNT.getMetric().overTime(resolution).apply(c);
            
            for (int i=0; i<totalCost.size(); i++) {
                var tcst = totalCost.get(i);
                points.add(new Point2D(tcst.getX(), (double)tcst.getY()/clickCount.get(i).getY()));
            }

            return points;
        };
    }
}
