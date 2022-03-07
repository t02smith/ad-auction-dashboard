package ad.auction.dashboard.model.Campaigns;

import java.util.List;
import java.util.stream.Stream;

import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;

/**
 * A campaign must consist of files for the impressions, clicks and server logs
 */
public class Campaign {

    // Lists of values read from file
    // From a list we can generate any number of streams

    private final String name;
    boolean dataLoaded = false;

    final String impressionPath;
    List<Impression> impressions;

    final String clickPath;
    List<Click> clicks;

    final String serverPath;
    List<Server> server;

    public Campaign(String name, String impressionPath, String clickPath, String serverPath) {
        this.name = name;
        this.impressionPath = impressionPath;
        this.clickPath = clickPath;
        this.serverPath = serverPath;
    }

    // GETTERS

    public Stream<Impression> impressions() {
        return this.impressions.stream();
    }

    public Stream<Server> server() {
        return this.server.stream();
    }

    public Stream<Click> clicks() {
        return this.clicks.stream();
    }

    public String name() {
        return this.name;
    }

    public boolean dataLoaded() {
        return this.dataLoaded;
    }
}
