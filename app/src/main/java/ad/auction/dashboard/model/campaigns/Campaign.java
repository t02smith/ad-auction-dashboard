package ad.auction.dashboard.model.campaigns;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;
import javafx.geometry.Point2D;

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

    protected LocalDateTime start;
    protected LocalDateTime end;

    boolean dataLoaded = false;

    List<Impression> impressions;
    List<Click> clicks;
    List<Server> server;

    //Stores calculations that have already been run
    protected HashMap<Metrics, List<Point2D>> cache = new HashMap<>();

    //Stores info to display in the UI
    public record CampaignData(String name, String clkPath, String impPath, String svrPath, LocalDateTime start, LocalDateTime end) {
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
        this.impressions = null;
        this.clicks = null;
        this.server = null;
        this.dataLoaded = false;
        this.cache = new HashMap<>();
    }

    /**
     * Store a calculated result in memory
     * @param m The metric calculated
     * @param data The result of the calculation
     */
    public void cacheData(Metrics m, List<Point2D> data) {
        this.cache.put(m, data);
    }

    /**
     * Set the start/end dates for the date
     * @param start setting start/end date
     * @param value the date to change it to
     */
    public void setDate(boolean start, LocalDateTime value) {
        this.cache = new HashMap<>();
        if (start) this.start = value;
        else this.end = value;
    }

    // GETTERS

    public boolean isCached(Metrics m) {return this.cache.containsKey(m);}

    public List<Point2D> getData(Metrics m) {
        return this.cache.get(m);
    }

    public CampaignData getData() {
        return new CampaignData(name, clkPath, impPath, svrPath, start, end);
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
