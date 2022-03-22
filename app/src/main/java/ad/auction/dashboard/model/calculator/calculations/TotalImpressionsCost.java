package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class TotalImpressionsCost extends Metric {

    public TotalImpressionsCost() {
        super("Impressions Cost", "£");
    }

    @Override
    public Function<Campaign, Number> overall() {
        return c -> (double) Math.round(1000 * c.impressions()
                .parallel()
                .reduce(0.0, (acc, elem) -> acc + elem.impressionCost(), Double::sum)) / 1000;

    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();
            if (c.impressions().findAny().isEmpty()) return points;

            LocalDateTime[] start = new LocalDateTime[] {c.impressions().findFirst().get().dateTime()};
            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, start[0])};
            double[] counter = new double[] {0};

            points.add(new Point2D(0,0));
            c.impressions().forEach(imp -> {
                if (!imp.dateTime().isBefore(end[0])) {
                    points.add(new Point2D(Metric.getXCoordinate(resolution, start[0]),counter[0]));
                    start[0] = end[0];
                    end[0] = Metric.incrementDate(resolution, end[0]);
                }

                counter[0] += imp.impressionCost();
            });

            points.add(new Point2D(Metric.getXCoordinate(resolution, start[0]), counter[0]));
            return points;
        };
    }
}
