package ad.auction.dashboard.model.calculator;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;
import javafx.util.Pair;

/**
 * Manages the calculations
 * 
 * @author tcs1g20
 */
public class Calculator {

    private static final Logger logger = LogManager.getLogger(Calculator.class.getSimpleName());

    //Max calculations to be ran at once
    private static final int CALCULATOR_THREAD_COUNT = 10;
    
    private final ExecutorService executor = Executors.newFixedThreadPool(CALCULATOR_THREAD_COUNT);

    /**
     * Runs a calculation on a separate thread
     * @param campaign
     * @param metric
     * @param func
     * @return The future result
     */
    public Future<Object> runCalculation(Campaign campaign, Metrics metric, MetricFunction func) {
        logger.info("Running calculation {}:{} on {}", metric, func, campaign.name());

        if (!campaign.dataLoaded()) throw new IllegalAccessError("Data must be loaded before running calculations");
        
        switch (func) {
            case OVERALL:
                return executor.submit(new Calculation<Object>(metric.getMetric().overall(), campaign));
            case OVER_TIME:
                return executor.submit(new Calculation<ArrayList<Point2D>>(metric.getMetric().overTime(ChronoUnit.DAYS), campaign));
            default:
                throw new IllegalArgumentException("//");
        }


    }

    /**
     * Runs both the overall and overtime calculations for a campaign and metric
     * @param campaign
     * @param metric
     * @return a pair containing the future results
     */
    public Pair<Future<Object>,Future<Object>> runBothCalculations(Campaign campaign, Metrics metric) {
        logger.info("Running calculations for {} on {}", metric, campaign);

        return new Pair<Future<Object>,Future<Object>>(
            runCalculation(campaign, metric, MetricFunction.OVERALL), 
            runCalculation(campaign, metric, MetricFunction.OVER_TIME));
    }

    /**
     * Stops all currently running calculations
     */
    public void close() {
        logger.info("Shutting down Calculator");
        this.executor.shutdown();
    }

}
