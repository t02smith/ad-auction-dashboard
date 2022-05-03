package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;


public class ClickCount extends Metric {

    public ClickCount() {
        super("Number of Clicks",
                "count",
                "The number of times an ad was click on");
    }
    
    @Override
    public Function<Campaign, Number> overall() {
        return c -> c.clicks().count();
    }

    @Override
    public Function<Campaign, List<Point2D>> overTime(ChronoUnit resolution, boolean isCumulative, int factor) {
        return c -> {
            var v = this.pointsSetup(resolution, factor, c.clicks().findFirst().get().dateTime());

            v.points.add(new Point2D(0,0));
            c.clicks().forEach(clk -> {
                if (!clk.dateTime().isBefore(v.end[0])) {
                    v.dCounter[0] += v.dCounter[1];
                    v.points.add(new Point2D(v.dCounter[0],v.lCounter[0]));
                    v.start[0] = v.end[0];
                    v.end[0] = Metric.incrementDate(resolution, v.end[0], factor);
                    if (!isCumulative) v.lCounter[0] = 0;
                }

                v.lCounter[0] += 1;
            });

            v.points.add(new Point2D(v.dCounter[0]+v.dCounter[1], v.lCounter[0]));
            return v.points;
        };
    }
}
