package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Campaign;

public class ConversionsCount implements Metric {

    @Override
    public Function<Campaign, ?> overall() {
        return c -> c.server()
                .filter(e -> e.conversion())
                .count();
    }
}
