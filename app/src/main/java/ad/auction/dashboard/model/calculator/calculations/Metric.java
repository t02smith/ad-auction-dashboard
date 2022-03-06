package ad.auction.dashboard.model.calculator.calculations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.files.FileType;
import javafx.geometry.Point2D;

/**
 * Each metric must provide:
 *  -> an overall value
 *  -> a set of graph points over time
 * 
 * @author tcs1g20
 */
public interface Metric {

    // public static Stream<Stream<Object>> splitToTimeResolution(Campaign campaign, ChronoUnit resolution, FileType type) {

    //     ArrayList<Stream<Object>> streams = new ArrayList<>();
    //     ArrayList<Object> currentStream = new ArrayList<>();

    //     LocalDateTime startPoint;

    //     switch (type) {
    //         case IMPRESSION:
    //             startPoint = campaign.impressions().findFirst().get().dateTime();

    //             campaign.impressions()
    //                     .forEach(i -> {
    //                         if (resolution.between(startPoint, i.dateTime()) >= 1) {
    //                             streams.add(currentStream.stream());
    //                             currentStream.clear();
    //                             currentStream.add(i);
                                
    //                         }
    //                     });
    //             break;
    //         case SERVER:
    //             break;
    //         case CLICK:
    //             break;
    //     }
    // }
    
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
    // public BiFunction<Campaign, ChronoUnit, HashSet<Point2D>> overTime();

    public enum MetricFunction {
        OVERALL,
        OVER_TIME;
    } 
}
