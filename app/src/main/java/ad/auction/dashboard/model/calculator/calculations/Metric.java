package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

/**
 * Each metric must provide:
 *  -> an overall value
 *  -> a set of graph points over time
 * 
 * @author tcs1g20
 */
public abstract class Metric {
    
    private final String displayName;

    public Metric(String displayName) {
        this.displayName = displayName;
    }

    public enum MetricFunction {
        OVERALL,
        OVER_TIME;
    } 

    /**
     * Calculate the overall metric
     * @return function for overall metric
     */
    public abstract Function<Campaign, Object> overall();

    /**
     * Calculate a set of graph points
     * (time, metric)
     * @return function for graph points
     */
    public abstract Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit timeResolution);

    public static double getXCoordinate(ChronoUnit resolution, LocalDateTime time) {
        switch (resolution) {
            case HOURS:
                return time.getHour();
            case WEEKS:
                return Math.round(time.getDayOfYear()/52);
            case DAYS:
            default:
                return time.getDayOfYear();
        }
    }

    public static LocalDateTime incrementDate(ChronoUnit resolution, LocalDateTime time) {
        switch (resolution) {
            case HOURS:
                return time.plusHours(1);
            case WEEKS:
                return time.plusDays(7);
            case DAYS:
            default:
                return time.plusDays(1);
        }
    }

    //GETTERS

    public String displayName() {
        return this.displayName;
    }
    
}
