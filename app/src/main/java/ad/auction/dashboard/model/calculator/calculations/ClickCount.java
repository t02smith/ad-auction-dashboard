package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Campaign;


public class ClickCount implements Metric {
    
    @Override
    public Function<Campaign, Object> overall() {
        return c -> c.clicks().count();
    }
}
