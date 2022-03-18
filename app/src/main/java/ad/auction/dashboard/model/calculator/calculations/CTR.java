package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class CTR extends Metric {

    public CTR() {
        super("Click Through Rate");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
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
            
            for (int i=1; i<impressionPts.size(); i++) {
                var clk = clickPts.get(i);
                points.add(new Point2D(clk.getX(), (double)clk.getY()/impressionPts.get(i).getY()));
            }

            return points;
        };
    }
}
