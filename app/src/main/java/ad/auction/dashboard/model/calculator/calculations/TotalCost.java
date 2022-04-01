package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.concurrent.Future;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class TotalCost extends Metric {

    public TotalCost() {
        super("Total Cost", "£");
    }

    @Override
    public Function<Campaign, Number> overall() {
        return c -> Utility.roundNDp((double) Metrics.TOTAL_COST_CLICK.getMetric().overall().apply(c) +
                (double) Metrics.TOTAL_COST_IMPRESSION.getMetric().overall().apply(c),3);

    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution, boolean isCumulative, int factor) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            
            Future<ArrayList<Point2D>> impCost = executor.submit(() -> Metrics.TOTAL_COST_IMPRESSION.getMetric().overTime(resolution, isCumulative, factor).apply(c));
            Future<ArrayList<Point2D>> clkCost = executor.submit(() -> Metrics.TOTAL_COST_CLICK.getMetric().overTime(resolution, isCumulative, factor).apply(c));
            while (!impCost.isDone() || !clkCost.isDone()) {}

            try {
                for (int i = 0; i < impCost.get().size(); i++) {
                    points.add(impCost.get().get(i).add(0, clkCost.get().get(i).getY()));
                }
            } catch (Exception ignored) {}

            return points;
        };
    }
}
