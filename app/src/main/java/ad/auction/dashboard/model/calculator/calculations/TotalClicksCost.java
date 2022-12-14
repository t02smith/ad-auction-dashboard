package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import ad.auction.dashboard.model.Util;
import ad.auction.dashboard.model.calculator.Histogram;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class TotalClicksCost extends Metric implements Histogram {

    public TotalClicksCost() {
        super("Clicks Cost",
                "£",
                "The total cost for all clicks on an ad.");
    }

    @Override
    public Function<Campaign, Number> overall() {
        return c -> Util.roundNDp(c.clicks()
                .parallel()
                .reduce(0.0, (acc, elem) -> acc + elem.clickCost(), Double::sum),3);
    }


    @Override
    public Function<Campaign, List<Point2D>> overTime(ChronoUnit resolution, boolean isCumulative, int factor) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            if (c.clicks().findAny().isEmpty()) return points;

            LocalDateTime[] start = new LocalDateTime[] {c.clicks().findFirst().get().dateTime()};
            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, start[0],factor)};
            double[] x = new double[] {0, 1.0/factor};
            double[] counter = new double[] {0};

            points.add(new Point2D(0,0));
            c.clicks().forEach(clk -> {
                if (!clk.dateTime().isBefore(end[0])) {
                    x[0] += x[1];
                    points.add(new Point2D(x[0],counter[0]));
                    start[0] = end[0];
                    end[0] = Metric.incrementDate(resolution, end[0], factor);
                    if (!isCumulative) counter[0] = 0;
                }

                counter[0] += clk.clickCost();
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
            c.clicks().forEach(clk -> {
                if (clk.clickCost() > max[0]) max[0] = clk.clickCost();
            });

            double step = Util.roundNDp(max[0]/Histogram.DISTRIBUTION_GROUPS, 5);
            for (double i=0; i<=step*(DISTRIBUTION_GROUPS+1); i+=step) {
                points.put(Util.roundNDp(i,5), 0L);
            }

            var steps = points.keySet().stream().sorted(Comparator.reverseOrder()).toList();

            c.clicks().forEach(clk -> {
                for (double s: steps) {
                    if (clk.clickCost() >= s) {
                        points.put(s, points.get(s)+1);
                        break;
                    }
                }
            });

            List<Point2D> pointsLs = new ArrayList<>();
            for (int i=0; i<Histogram.DISTRIBUTION_GROUPS; i++) {
                var curr = Util.roundNDp(i*step, 5);
                var next = Util.roundNDp(i*step+step, 5);
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
