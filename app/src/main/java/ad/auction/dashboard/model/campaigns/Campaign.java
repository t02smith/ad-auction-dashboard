package ad.auction.dashboard.model.campaigns;

import java.util.List;
import java.util.stream.Stream;

import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;

/**
 * A campaign must consist of files for the impressions, clicks and server logs
 * It will also contain the data but this will only be fetched when required
 * 
 * @author tcs1g20
 */
public class Campaign {

    // Lists of values read from file
    // From a list we can generate any number of streams

    private final String name;
    boolean dataLoaded = false;

    //Paths and data for all files
    final String impressionPath;
    List<Impression> impressions;

    final String clickPath;
    List<Click> clicks;

    final String serverPath;
    List<Server> server;

    //Stores info to display in the UI
    public record CampaignData(String name, String clkPath, String impPath, String svrPath) {
    }

    public Campaign(String name, String impressionPath, String clickPath, String serverPath) {
        this.name = name;
        this.impressionPath = impressionPath;
        this.clickPath = clickPath;
        this.serverPath = serverPath;
    }

    /**
     * Clear out the data currently stored data
     * to free up memory
     */
    public void flushData() {
        this.impressions.clear();
        this.clicks.clear();
        this.server.clear();
    }

    

    // GETTERS

    public CampaignData getData() {
        return new CampaignData(this.name, this.clickPath, this.impressionPath, this.serverPath);
    }

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
