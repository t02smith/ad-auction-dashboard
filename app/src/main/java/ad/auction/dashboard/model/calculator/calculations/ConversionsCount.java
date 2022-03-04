package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;

public class ConversionsCount implements Metric {

    @Override
    public Function<Campaign, Object> overall() {
        return c -> c.server()
                .filter(e -> e.conversion())
                .count();
    }
}
