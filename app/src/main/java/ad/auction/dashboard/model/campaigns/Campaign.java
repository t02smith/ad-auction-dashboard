package ad.auction.dashboard.model.campaigns;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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

    //Campaign info
    protected String name;
    protected String clkPath;
    protected String impPath;
    protected String svrPath;

    protected LocalDateTime start;
    protected LocalDateTime end;

    //File data
    boolean dataLoaded = false;
    protected List<Impression> impressions;
    protected List<Click> clicks;
    protected List<Server> server;

    //Stores calculations that have already been run
    protected HashMap<Metrics, List<Point2D>> cache = new HashMap<>();
    protected boolean cacheValues = true;

    //Stores info to display in the UI
    public record CampaignData(String name, String clkPath, String impPath, String svrPath, LocalDateTime start, LocalDateTime end) {
        //Start/end dates are only calculated when loaded so are excluded from the comparison

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CampaignData that = (CampaignData) o;
            return name.equals(that.name) && clkPath.equals(that.clkPath) && impPath.equals(that.impPath) && svrPath.equals(that.svrPath);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, clkPath, impPath, svrPath);
        }
    }

    /**
     * Generate a new campaign
     *
     * @param name           the name of the campaign
     * @param clickPath      the filepath of the clicks log
     * @param impressionPath the filepath of the impressions log
     * @param serverPath     the filepath of the server log
     */
    public Campaign(String name, String clickPath, String impressionPath, String serverPath) {
        this.name = name;
        this.impPath = impressionPath;
        this.clkPath = clickPath;
        this.svrPath = serverPath;
    }

    /**
     * Clears the calculated values cache
     */
    public void clearCache() {
        this.cache = new HashMap<>();
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
        this.clearCache();
    }

    /**
     * Store a calculated result in memory
     * @param m The metric calculated
     * @param data The result of the calculation
     */
    public void cacheData(Metrics m, List<Point2D> data) {
        if (this.cacheValues)
            this.cache.put(m, data);
    }

    /**
     * Set the start/end dates for the date
     * @param start setting start/end date
     * @param value the date to change it to
     */
    public void setDate(boolean start, LocalDateTime value) throws IllegalArgumentException {
        this.cache = new HashMap<>();
        if (start) {
            if (this.end != null)
                if (value.isAfter(this.end)) throw new IllegalArgumentException("Start date cannot be after end date");
            this.start = value;
        }
        else {
            if (this.start != null)
                if (value.isBefore(this.start)) throw new IllegalArgumentException("End date cannot be before start date");
            this.end = value;
        }
    }

    /* SETTERS */

    /**
     * Set the impression log data
     * @param imps impression log data
     */
    void setImpressions(List<Impression> imps) {
        this.impressions = imps;
    }

    /**
     * Set the server log data
     * @param svrs sever log data
     */
    void setServer(List<Server> svrs) {
        this.server = svrs;
    }

    /**
     * Set the click log data
     * @param clks click log data
     */
    void setClicks(List<Click> clks) {
        this.clicks = clks;
    }

    /**
     * Whether to store calculated values temporarily
     * @param state cache value (true)/ don't cache values (false)
     */
    public void setCacheValues(boolean state) {
        this.cacheValues = state;
    }

    // GETTERS

    /**
     * Whether there is a cache value for a given metric
     * @param m the metric being checked
     * @return metric cached (true) / metric not cached (false)
     */
    public boolean isCached(Metrics m) {
        return this.cache.containsKey(m);
    }

    /**
     * Get the cached data for a given metric
     * @param m the metric to get from cache
     * @return the list of points cached for that metric
     */
    public List<Point2D> getCacheData(Metrics m) {
        return this.cache.get(m);
    }

    /**
     * @return get data record for this campaign
     */
    public CampaignData getData() {
        return new CampaignData(name, clkPath, impPath, svrPath, start, end);
    }

    /**
     * @return stream of impression log records
     */
    public Stream<Impression> impressions() {
        return this.impressions.stream();
    }

    /**
     * @return stream of server log records
     */
    public Stream<Server> server() {
        return this.server.stream();
    }

    /**
     * @return stream of click log records
     */
    public Stream<Click> clicks() {
        return this.clicks.stream();
    }

    /**
     * @return campaign name
     */
    public String name() {
        return this.name;
    }

    /**
     * @return whether the data has been read from the log files
     */
    public boolean dataLoaded() {
        return this.dataLoaded;
    }

    /**
     * @return the impression log file path
     */
    public String impPath() {
        return this.impPath;
    }

    /**
     * @return the click log file path
     */
    public String clkPath() {
        return this.clkPath;
    }

    /**
     * @return the server log file path
     */
    public String svrPath() {
        return this.svrPath;
    }
}
