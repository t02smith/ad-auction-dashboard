package ad.auction.dashboard.model.calculator;

import java.util.concurrent.Callable;
import java.util.function.Function;

import ad.auction.dashboard.model.campaigns.Campaign;

/**
 * Calculation
 * Will run a given calculation on its own thread
 * 
 * @author tcs1g20
 */
public class Calculation<T> implements Callable<Object> {
    
    //The calculation to run
    private final Function<Campaign, T> calculation;

    //The input to the calculation
    private final Campaign campaign;

    /**
     * Create a new calculation to be called
     * @param calculation
     * @param c
     */
    public Calculation(Function<Campaign, T> calculation, Campaign c) {
        this.calculation = calculation;
        this.campaign = c;       
    }

    @Override
    public T call() throws Exception {
        return calculation.apply(campaign);
    }
}
