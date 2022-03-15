package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

public class TotalClicksCost extends Metric {

    public TotalClicksCost() {
        super("Clicks Cost");
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
            LocalDateTime[] end = new LocalDateTime[] {
                Metric.incrementDate(resolution, c.clicks().findFirst().get().dateTime())};
            double[] counter = new double[] {0};

            c.clicks().forEach(clk -> {
                if (!clk.dateTime().isBefore(end[0])) {
                    points.add(new Point2D(Metric.getXCoordinate(resolution, clk.dateTime()),counter[0]));
                    end[0] = Metric.incrementDate(resolution, end[0]);
                }

                counter[0] += clk.clickCost();
            });

            points.add(new Point2D(Metric.getXCoordinate(resolution, end[0]), counter[0]));
            return points;
        };
    }
}
