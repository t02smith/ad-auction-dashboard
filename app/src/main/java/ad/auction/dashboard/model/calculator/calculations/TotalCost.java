package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.function.Function;

import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class TotalCost extends Metric {

    public TotalCost() {
        super("Total Cost");
    }

    @Override
    public Function<Campaign, Object> overall() {
        return c -> (double) Metrics.TOTAL_COST_CLICK.getMetric().overall().apply(c) +
                (double) Metrics.TOTAL_COST_IMPRESSION.getMetric().overall().apply(c);

    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            
            ArrayList<Point2D> impCost = Metrics.TOTAL_COST_IMPRESSION.getMetric().overTime(resolution).apply(c);
            ArrayList<Point2D> clkCost = Metrics.TOTAL_COST_CLICK.getMetric().overTime(resolution).apply(c);
  
            for (int i=0; i<impCost.size(); i++) {
                points.add(impCost.get(i).add(0, clkCost.get(i).getY()));
            }

            return points;
        };
    }
}
