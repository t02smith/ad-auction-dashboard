package ad.auction.dashboard.model.calculator;

import java.util.concurrent.Callable;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Calculation
 * Will run a given calculation on its own thread
 * 
 * @author tcs1g20
 */
public class Calculation<T> implements Callable<Object> {

    private static final Logger logger = LogManager.getLogger(Calculator.class.getSimpleName());

    //The calculation to run
    private final Function<Campaign, T> calculation;

    //The input to the calculation
    private final Campaign campaign;

    /**
     * Create a new calculation to be called
     * @param calculation the function to run
     * @param c the campaign to run it on
     */
    public Calculation(Function<Campaign, T> calculation, Campaign c) {
        this.calculation = calculation;
        this.campaign = c;       
    }

    @Override
    public T call() throws NullPointerException, IllegalStateException {
        if (campaign == null) throw new NullPointerException("Null campaign given");
        if (calculation == null) throw new NullPointerException("Null calculation function given");
        if (!campaign.dataLoaded()) throw new IllegalStateException("Campaign data not loaded");

        var t = System.currentTimeMillis();
        var res = calculation.apply(campaign);
        logger.info("{}: Calculation ran in {}ms", campaign.name(), System.currentTimeMillis()-t);
        return res;
    }
}
