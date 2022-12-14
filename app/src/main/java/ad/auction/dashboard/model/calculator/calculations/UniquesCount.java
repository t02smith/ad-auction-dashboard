package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import ad.auction.dashboard.model.files.records.Click;
import javafx.geometry.Point2D;

public class UniquesCount extends Metric {

    public UniquesCount() {
        super("Number of Uniques",
                "count",
                "How many unique users click on an ad.");
    }

    @Override
    public Function<Campaign, Number> overall() {
        return c -> c.clicks()
                     .map(Click::ID)
                     .distinct()
                     .count();
    }

    @Override
    public Function<Campaign, List<Point2D>> overTime(ChronoUnit resolution, boolean isCumulative, int factor) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            if (c.clicks().findFirst().isEmpty()) return points;

            LocalDateTime[] start = new LocalDateTime[] {c.clicks().findFirst().get().dateTime()};
            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, start[0],factor)};
            double[] x = new double[] {0, 1.0/factor};
            long[] counter = new long[] {0};

            points.add(new Point2D(0,0));
            HashSet<Long> seenIDs = new HashSet<>();
            c.clicks().forEach(clk -> {
                if (!clk.dateTime().isBefore(end[0])) {
                    x[0] += x[1];
                    points.add(new Point2D(x[0],counter[0]));
                    start[0] = end[0];
                    end[0] = Metric.incrementDate(resolution, end[0], factor);
                    if (!isCumulative) counter[0] = 0;
                }

                if (!seenIDs.contains(clk.ID())) {
                    counter[0] += 1;
                    seenIDs.add(clk.ID());
                }
            });

            points.add(new Point2D(x[0]+x[1], counter[0]));
            return points;
        };
    }
}
