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

public class TotalClicksCost extends Metric implements Histogram {

    public TotalClicksCost() {
        super("Clicks Cost", "£");
    }

    @Override
    public Function<Campaign, Number> overall() {
        return c -> Utility.roundNDp(c.clicks()
                .parallel()
                .reduce(0.0, (acc, elem) -> acc + elem.clickCost(), Double::sum),3);
    }


    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            if (c.clicks().findAny().isEmpty()) return points;

            LocalDateTime[] start = new LocalDateTime[] {c.clicks().findFirst().get().dateTime()};
            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, start[0])};
            double[] counter = new double[] {0};

            points.add(new Point2D(0,0));
            c.clicks().forEach(clk -> {
                if (!clk.dateTime().isBefore(end[0])) {
                    points.add(new Point2D(Metric.getXCoordinate(resolution, start[0]),counter[0]));
                    start[0] = end[0];
                    end[0] = Metric.incrementDate(resolution, end[0]);
                }

                counter[0] += clk.clickCost();
            });

            points.add(new Point2D(Metric.getXCoordinate(resolution, start[0]), counter[0]));

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

            double step = max[0]/5;
            for (double i=0; i<=max[0]+step; i+=step) {
                points.put(i, 0L);
            }

            var steps = points.keySet().stream().sorted(Comparator.reverseOrder()).toList();

            c.clicks().forEach(clk -> {
                steps.forEach(s -> {
                    if (clk.clickCost() >= s) points.put(s, points.get(s)+1);
                });
            });

            List<Point2D> pointsLs = new ArrayList<>();

            for (int i=0; i<5; i++) {
                pointsLs.add(new Point2D(i*step, 0));
                pointsLs.add(new Point2D(i*step, points.get(i*step)));
                pointsLs.add(new Point2D((i+1)*step, points.get(i*step)));
                pointsLs.add(new Point2D((i+1)*step, points.get((i+1)*step)));
                pointsLs.add(new Point2D((i+1)*step, 0));
            }

            return pointsLs;
        };
    }
}
