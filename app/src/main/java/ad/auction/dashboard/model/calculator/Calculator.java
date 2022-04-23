package ad.auction.dashboard.model.calculator;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Point2D;

/**
 * Manages the calculations
 * 
 * @author tcs1g20
 */
public class Calculator {

    private static final Logger logger = LogManager.getLogger(Calculator.class.getSimpleName());

    // Max calculations to be ran at once
    private static final int CALCULATOR_THREAD_COUNT = 15;

    private final ExecutorService executor = Executors.newFixedThreadPool(CALCULATOR_THREAD_COUNT);

    private boolean cumulative = true;
    private ChronoUnit timeResolution = ChronoUnit.DAYS;

    //The impression loaded when first launching a campaign
    private Metrics defaultMetric = Metrics.IMPRESSION_COUNT;

    public Future<Object> runCalculation(Campaign campaign, Metrics metric, MetricFunction func) {
        return runCalculation(campaign,metric,func,1);
    }

    /**
     * Runs a calculation on a separate thread
     * @param campaign the data to use
     * @param metric the type of calculation
     * @param func overall/over time
     * @return The future result
     */
    @SuppressWarnings("unchecked")
    public Future<Object> runCalculation(Campaign campaign, Metrics metric, MetricFunction func, int factor) {
        logger.info("Running calculation {}:{} on {}", metric, func, campaign.name());

        if (!campaign.dataLoaded())
            throw new IllegalAccessError("Data must be loaded before running calculations");

        switch (func) {
            case OVERALL:
                return executor.submit(new Calculation<>(metric.getMetric().overall(), campaign));
            case OVER_TIME:
                if (campaign.isCached(metric)) {
                    logger.info("{} collected from cache", metric);
                    return executor.submit(() -> campaign.getData(metric));
                }

                var res = executor.submit(
                        new Calculation<>(metric.getMetric().overTime(timeResolution, cumulative, factor), campaign));

                executor.submit(() -> {
                    while (!res.isDone()) {}
                    try {
                        campaign.cacheData(metric, (List<Point2D>)res.get());
                        logger.info("Caching {}", metric);
                    } catch (ExecutionException | InterruptedException e) {logger.error("Error caching {}: {}", metric, e.getMessage());}
                });

                return res;
            case HISTOGRAM:
                return executor.submit(
                        new Calculation<>(((Histogram)metric.getMetric()).histogram(), campaign));
            default:
                throw new IllegalArgumentException("//");
        }
    }

    public HashMap<Metrics, Number> dashboardValues(Campaign campaign) {
        HashMap<Metrics, Number> db = new HashMap<>();

        var data = Arrays.stream(Metrics.values())
                .map(m -> runCalculation(campaign, m, MetricFunction.OVERALL));

        while (!data.allMatch(Future::isDone)) {}

        var ls = (Number[])data.toArray();
        for (int i = 0; i < ls.length; i++) {
            db.put(Metrics.values()[i], ls[i]);
            logger.info(Metrics.values()[i] + " " + ls[i]);
        }

        return db;
    }

    //SETTERS

    public void setDefaultMetric(Metrics m) {
        logger.info("Updating default metric to {}", m);
        this.defaultMetric = m;
    }

    public Metrics getDefaultMetric() {
        return this.defaultMetric;
    }

    public void setCumulative(boolean state) {
        this.cumulative = state;
    }

    public void setTimeResolution(ChronoUnit res) {
        switch (res) {
            case DAYS, HOURS, WEEKS -> this.timeResolution = res;
        }
    }
}
