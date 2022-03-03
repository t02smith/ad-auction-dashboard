package ad.auction.dashboard.model.calculator.calculations;

import java.util.function.Function;

import ad.auction.dashboard.model.files.FileType;
import ad.auction.dashboard.model.files.records.Campaign;

public class UniquesCount implements Metric {

    @Override
    public Function<Campaign, Object> overall() {
        throw new IllegalArgumentException("You must provde a filetype");
    }

    public Function<Campaign, Object> overall(FileType type) {
        switch (type) {
            case IMPRESSION:
                return c -> c.impressions()
                        .map(elem -> elem.ID())
                        .distinct()
                        .count();
            case SERVER:
                return c -> c.server()
                        .map(elem -> elem.ID())
                        .distinct()
                        .count();
            case CLICK:
                return c -> c.clicks()
                        .map(elem -> elem.ID())
                        .distinct()
                        .count();
            default:
                throw new IllegalArgumentException("Unrecognised file type");
        }
    }
}
