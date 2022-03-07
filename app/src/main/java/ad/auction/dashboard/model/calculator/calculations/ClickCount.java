package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;
import javafx.geometry.Point2D;


public class ClickCount implements Metric {
    
    @Override
    public Function<Campaign, Object> overall() {
        return c -> c.clicks().count();
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, c.clicks().findFirst().get().dateTime())};
            long[] counter = new long[] {0};
            
            c.clicks().forEach(clk -> {
                if (!clk.dateTime().isBefore(end[0])) {
                    points.add(new Point2D(Metric.getXCoordinate(resolution, clk.dateTime()),counter[0]));
                    end[0] = Metric.incrementDate(resolution, end[0]);
                }

                counter[0] += 1;
                
            });

            points.add(new Point2D(Metric.getXCoordinate(resolution, end[0]), counter[0]));
            return points;
        };
    }
}
