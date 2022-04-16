package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class BouncesCount extends Metric {

    public static final long BOUNCE_MAX_TIME_SEC = 60;

    public BouncesCount() {
        super("Number of Bounces",
            "count",
            "The number of users who click on an ad, but then fail to interact with the website (i.e. leaving after a short time or only visiting one page");
    }

    @Override
    public Function<Campaign, Number> overall() {
        return c -> c.server()
                .filter(e -> e.pagesViewed() == 1 || e.secondsOnWebsite() < BOUNCE_MAX_TIME_SEC)
                .count();
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution, boolean isCumulative, int factor) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            if (c.server().findAny().isEmpty()) return points;

            LocalDateTime[] start = new LocalDateTime[] {c.server().findFirst().get().dateTime()};
            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, start[0], factor)};
            double[] x = new double[] {0, 1.0/factor};
            long[] counter = new long[] {0};

            points.add(new Point2D(0,0));
            c.server().forEach(svr -> {
                if (!svr.dateTime().isBefore(end[0])) {
                    x[0] += x[1];
                    points.add(new Point2D(x[0], counter[0]));
                    start[0] = end[0];
                    end[0] = Metric.incrementDate(resolution, end[0], factor);
                    if (isCumulative) counter[0] = 0;
                }

                if (svr.pagesViewed() == 1 || svr.secondsOnWebsite() < BOUNCE_MAX_TIME_SEC) {
                    counter[0] += 1;
                }
            });

            points.add(new Point2D(x[0]+x[1], counter[0]));
            return points;
        };
    }
}
