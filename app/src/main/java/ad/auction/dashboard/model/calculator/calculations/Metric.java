package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Campaign;

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
    public Function<Campaign, ?> overall();

    // /**
    //  * Calculate a set of graph points
    //  * (time, metric)
    //  * @return function for graph points
    //  */
    // public Function<Bundle, HashSet<Point2D>> overTime();
}
