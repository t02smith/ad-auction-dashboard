package ad.auction.dashboard.model.calculator;

import java.util.concurrent.Callable;
import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;

/**
 * Calculation
 * Will run a given calculation on its own thread
 * 
 * @author tcs1g20
 */
public class Calculation implements Callable<Object> {
    
    //The calculation to run
    private final Function<Campaign, Object> calculation;

    //The input to the calculation
    private final Campaign campaign;

    /**
     * Create a new calculation to be called
     * @param calculation
     * @param c
     */
    public Calculation(Function<Campaign, Object> calculation, Campaign c) {
        this.calculation = calculation;
        this.campaign = c;
    }

    @Override
    public Object call() throws Exception {
        return calculation.apply(campaign);
    }
}
