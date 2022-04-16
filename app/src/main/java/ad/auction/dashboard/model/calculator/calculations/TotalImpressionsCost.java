package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.calculator.Histogram;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class TotalImpressionsCost extends Metric implements Histogram {

    public TotalImpressionsCost() {
        super("Impressions Cost",
                "£",
                "The cost for all the ad impressions");
    }

    @Override
    public Function<Campaign, Number> overall() {
        return c -> (double) Math.round(1000 * c.impressions()
                .parallel()
                .reduce(0.0, (acc, elem) -> acc + elem.impressionCost(), Double::sum)) / 1000;

    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution, boolean isCumulative, int factor) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            if (c.impressions().findAny().isEmpty()) return points;

            LocalDateTime[] start = new LocalDateTime[] {c.impressions().findFirst().get().dateTime()};
            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, start[0],factor)};
            double[] x = new double[] {0, 1.0/factor};
            double[] counter = new double[] {0};

            points.add(new Point2D(0,0));
            c.impressions().forEach(imp -> {
                if (!imp.dateTime().isBefore(end[0])) {
                    x[0] += x[1];
                    points.add(new Point2D(x[0],counter[0]));
                    start[0] = end[0];
                    end[0] = Metric.incrementDate(resolution, end[0]);
                    if (isCumulative) counter[0] = 0;
                }

                counter[0] += imp.impressionCost();
            });

            points.add(new Point2D(x[0]+x[1], counter[0]));
            return points;
        };
    }

    @Override
    public Function<Campaign, List<Point2D>> histogram() {
        return c -> {
            HashMap<Double, Long> points = new HashMap<>();

            //Find max value
            double[] max = new double[] {Double.MIN_VALUE};
            c.impressions().forEach(clk -> {
                if (clk.impressionCost() > max[0]) max[0] = clk.impressionCost();
            });

            double step = Utility.roundNDp(max[0]/Histogram.DISTRIBUTION_GROUPS, 5);
            for (double i=0; i<=step*(DISTRIBUTION_GROUPS+1); i+=step) {
                points.put(Utility.roundNDp(i,5), 0L);
            }

            var steps = points.keySet().stream().sorted(Comparator.reverseOrder()).toList();

            c.impressions().forEach(imp -> {
                for (double s: steps) {
                    if (imp.impressionCost() >= s) {
                        points.put(s, points.get(s)+1);
                        break;
                    }
                }
            });

            List<Point2D> pointsLs = new ArrayList<>();
            for (int i=0; i<Histogram.DISTRIBUTION_GROUPS; i++) {
                var curr = Utility.roundNDp(i*step, 5);
                var next = Utility.roundNDp(i*step+step, 5);
                pointsLs.add(new Point2D(curr, 0));
                pointsLs.add(new Point2D(curr, points.get(curr)));
                pointsLs.add(new Point2D(next, points.get(curr)));
                pointsLs.add(new Point2D(next, points.get(next)));
                pointsLs.add(new Point2D(next, 0));
            }

            return pointsLs;
        };
    }
}
