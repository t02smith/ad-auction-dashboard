package ad.auction.dashboard.model.files.records;

import java.util.List;
import java.util.stream.Stream;

/**
 * A campaign must consist of files for the impressions, clicks and server logs
 */
public class Campaign {

    private List<Impression> impressions;
    private List<Click> clicks;
    private List<Server> server;

    public Campaign(List<Impression> impressions, List<Click> clicks, List<Server> server) {
        this.impressions = impressions;
        this.clicks = clicks;
        this.server = server;
    }

    // GETTERS

    public Stream<Impression> impressions() {
        return this.impressions.stream();
    }

    public Stream<Click> clicks() {
        return this.clicks.stream();
    }

    public Stream<Server> server() {
        return this.server.stream();
    }
}


