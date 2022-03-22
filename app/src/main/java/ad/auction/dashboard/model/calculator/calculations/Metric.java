package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

/**
 * Each metric must provide:
 *  -> an overall value
 *  -> a set of graph points over time
 * 
 * Calculation references can be found in the spec
 * 
 * @author tcs1g20
 */
public abstract class Metric {
    
    protected final String displayName;
    protected final String unit;

    protected final ExecutorService executor = Executors.newFixedThreadPool(2);

    public Metric(String displayName, String unit) {
        this.displayName = displayName;
        this.unit = unit;
    }

    public enum MetricFunction {
        OVERALL,
        OVER_TIME
    } 

    /**
     * Calculate the overall metric
     * @return function for overall metric
     */
    public abstract Function<Campaign, Number> overall();

    /**
     * Calculate a set of graph points
     * (time, metric)
     * @return function for graph points
     */
    public abstract Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit timeResolution);

    public static double getXCoordinate(ChronoUnit resolution, LocalDateTime time) {
        return switch (resolution) {
            case HOURS -> time.getHour();
            case WEEKS -> Math.round(time.getDayOfYear() / 52);
            case DAYS -> time.getDayOfYear();
            default -> throw new IllegalStateException("Unexpected value: " + resolution);
        };
    }

    public static LocalDateTime incrementDate(ChronoUnit resolution, LocalDateTime time) {
        return incrementDate(resolution, time, 1);
    }

    public static LocalDateTime incrementDate(ChronoUnit resolution, LocalDateTime time, int factor) {
        return switch (resolution) {
            case HOURS -> time.plusHours(factor);
            case WEEKS -> time.plusDays(7 * factor);
            case DAYS -> time.plusDays(factor);
            default -> throw new IllegalStateException("Unexpected value: " + resolution);
        };
    }

    //GETTERS

    public String displayName() {
        return this.displayName;
    }

    public String unit() {return this.unit;}
    
}
