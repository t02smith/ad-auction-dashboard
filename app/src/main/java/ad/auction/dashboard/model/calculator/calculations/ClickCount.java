package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;


public class ClickCount extends Metric {

    public ClickCount() {
        super("Number of Clicks", "count");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
        return c -> c.clicks().count();
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            if (c.clicks().findAny().isEmpty()) return points;

            LocalDateTime[] start = new LocalDateTime[] {c.clicks().findFirst().get().dateTime()};
            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, start[0])};
            long[] counter = new long[] {0};

            points.add(new Point2D(0,0));
            c.clicks().forEach(clk -> {
                if (!clk.dateTime().isBefore(end[0])) {
                    points.add(new Point2D(Metric.getXCoordinate(resolution, start[0]),counter[0]));
                    start[0] = end[0];
                    end[0] = Metric.incrementDate(resolution, end[0]);
                }

                counter[0] += 1;
            });

            points.add(new Point2D(Metric.getXCoordinate(resolution, start[0]), counter[0]));
            return points;
        };
    }
}
