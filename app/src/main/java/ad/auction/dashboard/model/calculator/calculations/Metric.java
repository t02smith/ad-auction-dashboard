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

    //Metric information
    protected final String displayName;
    protected final String description;
    protected final String unit;

    protected final ExecutorService executor = Executors.newFixedThreadPool(2);

    /**
     * Generate a new metric
     * @param displayName the metrics name to be displayed to the user
     * @param unit the y-axis unit of the metric
     * @param description a description of what the metric shows
     */
    public Metric(String displayName, String unit, String description) {
        this.displayName = displayName;
        this.unit = unit;
        this.description = description;
    }

    //Different functions for each metric
    public enum MetricFunction {
        OVERALL,
        OVER_TIME,
        HISTOGRAM
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
    public abstract Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit timeResolution, boolean cumulative, int factor);

    /**
     * Gets the next date to calculate a point for
     * @param resolution The timescale to increase by
     * @param time the last time point
     * @param factor how many points per time resolution
     * @return the next date
     */
    public static LocalDateTime incrementDate(ChronoUnit resolution, LocalDateTime time, int factor) {
        return switch (resolution) {
            case HOURS -> time.plusHours(factor);
            case WEEKS -> time.plusDays(7L * factor);
            case DAYS -> time.plusHours(24/factor);
            default -> throw new IllegalStateException("Unexpected value: " + resolution);
        };
    }

    //GETTERS

    /**
     * @return the metrics display name
     */
    public String displayName() {
        return this.displayName;
    }

    /**
     * @return the metrics y-axis unit
     */
    public String unit() {
        return this.unit;
    }

    /**
     * @return the metrics description
     */
    public String desc() {
        return this.description;
    }
    
}
