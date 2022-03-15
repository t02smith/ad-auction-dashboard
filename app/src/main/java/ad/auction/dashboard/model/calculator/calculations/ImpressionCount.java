package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;


public class ImpressionCount extends Metric {
    
    public ImpressionCount() {
        super("Number of Impressions");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
        return c -> c.impressions().count();
    }

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit resolution) {
        return c -> {
            ArrayList<Point2D> points = new ArrayList<>();

            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, c.impressions().findFirst().get().dateTime())};
            long[] counter = new long[] {0};
            
            c.impressions().forEach(imp -> {
                if (!imp.dateTime().isBefore(end[0])) {
                    points.add(new Point2D(Metric.getXCoordinate(resolution, imp.dateTime()),counter[0]));
                    end[0] = Metric.incrementDate(resolution, end[0]);
                }

                counter[0] += 1;
                
            });

            points.add(new Point2D(Metric.getXCoordinate(resolution, end[0]), counter[0]));
            return points;
        };
    }

    
}
