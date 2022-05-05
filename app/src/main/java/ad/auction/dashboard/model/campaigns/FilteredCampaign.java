package ad.auction.dashboard.model.campaigns;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import ad.auction.dashboard.model.files.records.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;

/**
 * A filtered campaign applies filters to the data before returning it
 * Filters are identified by their hashCode.
 * 
 * The view will specify what the filter is initially and then reference
 * it using the hashcode
 *
 * @author tcs1g20
 */
public class FilteredCampaign extends Campaign {

    private static final Logger logger = LogManager.getLogger(FilteredCampaign.class.getSimpleName());

    //active filters
    protected HashMap<Integer, Boolean> filterActive = new HashMap<>();
    protected HashMap<Integer, Predicate<User>> userFilters = new HashMap<>();

    //User data
    protected HashMap<Long, User> userData;

    /**
     * Generate a new filtered campaign
     * @param name the name of the campaign
     * @param impressionPath the filepath of the impressions log
     * @param clickPath the filepath of the clicks log
     * @param serverPath the filepath of the server log
     */
    public FilteredCampaign(String name, String impressionPath, String clickPath, String serverPath) {
        super(name, clickPath, impressionPath, serverPath);
    }

    /**
     * Turns a given filter on/off
     * @param filterHash The filter to toggle
     */
    public void toggleFilter(int filterHash) {
        if (filterActive.containsKey(filterHash)) {
            filterActive.replace(filterHash, !filterActive.get(filterHash));
        }

        this.cache = new HashMap<>();
        logger.info("Cache cleared");
        logger.info("Toggling filter {} to {}", filterHash, filterActive.get(filterHash));
    }

    /**
     * Set all filters on/off
     * @param state on/off
     */
    public void toggleAllFilters(boolean state) {
        this.filterActive.keySet().forEach(k -> filterActive.put(k, state));
        this.cache = new HashMap<>();
        logger.info("Cache cleared");
        logger.info("Toggled all filters to {}", state);
    }

    /**
     * Add a filter to just the impressions data
     * e.g. Gender,
     * @param filter Set the filter for impressions
     */
    public int addUserFilter(Predicate<User> filter) {
        int hash = filter.hashCode();
        if (!userFilters.containsKey(hash)) {
            this.userFilters.put(hash, filter);
            this.filterActive.put(hash, false);
        }

        return hash;
    }

    /**
     * Take user data from impression dataset to be used in filters
     * @param imps the impression dataset
     * @return the <id, User> data
     */
    private HashMap<Long, User> collectUserData(List<Impression> imps) {
        logger.info("Collecting user data");
        HashMap<Long, User> users = new HashMap<>();

        imps.forEach(i -> {
            if (users.containsKey(i.ID())) return;
            users.put(i.ID(), new User(i.gender(),i.ageGroup(),i.income(),i.context()));
        });

        logger.info("User data collected");
        return users;
    }

    /* SETTERS */

    /**
     * Set the impression data
     * Collects user data for filtering
     * @param imps impression log data
     */
    @Override
    void setImpressions(List<Impression> imps) {
        super.setImpressions(imps);
        this.userData = this.collectUserData(imps);
    }

    // Getters

    /**
     * @return a snapshot of the current filter settings
     */
    public CampaignSnapshot generateSnapshot() {
        return new CampaignSnapshot(this);
    }


    /**
     * @return stream of click data records with filters applied
     */
    @Override
    public Stream<Click> clicks() {
        return this.clicks.stream()
                .dropWhile(r -> r.dateTime().isBefore(start))
                .takeWhile(r -> r.dateTime().isBefore(end) || r.dateTime().equals(end))
                .filter(c -> userFilters.keySet().stream().filter(filterActive::get)
                                .allMatch(f -> userFilters.get(f).test(userData.get(c.ID()))));
    }

    /**
     * @return stream of impression data records with filters applied
     */
    @Override
    public Stream<Impression> impressions() {
        return this.impressions.stream()
                .dropWhile(r -> r.dateTime().isBefore(start))
                .takeWhile(r -> r.dateTime().isBefore(end) || r.dateTime().equals(end))
                .filter(i -> userFilters.keySet().stream().filter(filterActive::get)
                        .allMatch(f -> userFilters.get(f).test(userData.get(i.ID()))));
    }

    /**
     * @return impression list without filters
     */
    List<Impression> impressionsLs() {
        return this.impressions;
    }

    /**
     * @return stream of server data records with filters applied
     */
    @Override
    public Stream<Server> server() {
        return this.server.stream()
                .dropWhile(r -> r.dateTime().isBefore(start))
                .takeWhile(r -> r.dateTime().isBefore(end) || r.dateTime().equals(end))
                .filter(s -> userFilters.keySet().stream().filter(filterActive::get)
                        .allMatch(f -> userFilters.get(f).test(userData.get(s.ID()))));
    }

}
