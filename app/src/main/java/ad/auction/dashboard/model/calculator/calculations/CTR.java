package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.calculator.Metrics;
import javafx.geometry.Point2D;

public class CTR implements Metric {
    
    @Override
    public Function<Campaign, Object> overall() {
        return c -> {
            long clickCount = (long)Metrics.CLICK_COUNT.getMetric().overall().apply(c);
            long impressionCount = (long)Metrics.IMPRESSION_COUNT.getMetric().overall().apply(c);
            
            return Utility.roundNDp(((double)clickCount/impressionCount), 5);
        };
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {           
            ArrayList<Point2D> points = new ArrayList<>();

            ArrayList<Point2D> impressionPts = Metrics.IMPRESSION_COUNT.getMetric().overTime(resolution).apply(c);
            ArrayList<Point2D> clickPts = Metrics.CLICK_COUNT.getMetric().overTime(resolution).apply(c);
            
            for (int i=0; i<impressionPts.size(); i++) {
                var clk = clickPts.get(i);
                points.add(new Point2D(clk.getX(), (double)clk.getY()/impressionPts.get(i).getY()));
            }

            return points;
        };
    }
}
