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

    //Campaign data is package protected so the CampaignManager can
    // access it but nothing else can unless there is getter/setter    
    String name;
    String clkPath;
    String impPath;
    String svrPath;

    boolean dataLoaded = false;

    List<Impression> impressions;
    List<Click> clicks;
    List<Server> server;

    //Stores info to display in the UI
    public record CampaignData(String name, String clkPath, String impPath, String svrPath) {
    }

    public Campaign(String name, String impressionPath, String clickPath, String serverPath) {
        this.name = name;
        this.impPath = impressionPath;
        this.clkPath = clickPath;
        this.svrPath = serverPath;
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
        return new CampaignData(name, clkPath, impPath, svrPath);
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
