package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import ad.auction.dashboard.model.files.records.Server;
import javafx.geometry.Point2D;

public class ConversionsCount extends Metric {

    public ConversionsCount() {
        super("Number of Conversions", "count");
    }

    @Override
    public Function<Campaign, Number> overall() {
        return c -> c.server()
                .filter(Server::conversion)
                .count();
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> cumulative(ChronoUnit resolution, boolean isCumulative) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            if (c.server().findAny().isEmpty()) return points;

            LocalDateTime[] start = new LocalDateTime[] {c.server().findFirst().get().dateTime()};
            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, start[0])};
            long[] counter = new long[] {0};

            points.add(new Point2D(0,0));
            c.server().forEach(svr -> {
                if (!svr.dateTime().isBefore(end[0])) {
                    points.add(new Point2D(Metric.getXCoordinate(resolution, start[0]),counter[0]));
                    start[0] = end[0];
                    end[0] = Metric.incrementDate(resolution, end[0]);
                    if (isCumulative) counter[0] = 0;
                }

                if (svr.conversion()) counter[0] += 1;
            });

            points.add(new Point2D(Metric.getXCoordinate(resolution, start[0]), counter[0]));
            return points;
        };
    }
}
