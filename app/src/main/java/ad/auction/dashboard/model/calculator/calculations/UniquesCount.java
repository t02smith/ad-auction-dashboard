package ad.auction.dashboard.model.calculator.calculations;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.function.Function;

import ad.auction.dashboard.model.Campaigns.Campaign;
import ad.auction.dashboard.model.files.FileType;
import javafx.geometry.Point2D;

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

    @Override
    public Function<Campaign, ArrayList<Point2D>> overTime(ChronoUnit timeResolution) {
        // TODO Auto-generated method stub
        return null;
    }
}
