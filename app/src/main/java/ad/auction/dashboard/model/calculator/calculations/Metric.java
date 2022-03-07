package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;
import javafx.geometry.Point2D;

/**
 * Each metric must provide:
 *  -> an overall value
 *  -> a set of graph points over time
 * 
 * @author tcs1g20
 */
public interface Metric {
    
    /**
     * Calculate the overall metric
     * @return function for overall metric
     */
    public Function<Campaign, Object> overall();

    /**
     * Calculate a set of graph points
     * (time, metric)
     * @return function for graph points
     */
    public Function<Campaign, HashSet<Point2D>> overTime(ChronoUnit timeResolution);

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

    public enum MetricFunction {
        OVERALL,
        OVER_TIME;
    } 
}
