package ad.auction.dashboard.model.calculator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import javafx.util.Pair;

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
        
        Function<Campaign, Object> function;
        switch (func) {
            case OVERALL:
                function = metric.getMetric().overall();
                break;
            default:
                throw new IllegalArgumentException("//");
        }

        return executor.submit(new Calculation(function, campaign));

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

}
