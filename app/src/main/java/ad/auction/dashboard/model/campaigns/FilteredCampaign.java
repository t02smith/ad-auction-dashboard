package ad.auction.dashboard.model.campaigns;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
 */
public class FilteredCampaign extends Campaign {

    private static final Logger logger = LogManager.getLogger(FilteredCampaign.class.getSimpleName());

    //Currently active filters
    private final HashMap<Integer, Boolean> filterActive = new HashMap<>();

    private final HashMap<Integer, Predicate<Impression>> impFilters = new HashMap<>();

    public FilteredCampaign(String name, String impressionPath, String clickPath, String serverPath) {
        super(name, impressionPath, clickPath, serverPath);
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
     * Add a filter to just the impressions data
     * e.g. Gender,
     * @param filter Set the filter for impressions
     */
    public int addImpFilter(Predicate<Impression> filter) {
        int hash = filter.hashCode();
        if (!impFilters.containsKey(hash)) {
            this.impFilters.put(hash, filter);
            this.filterActive.put(hash, false);
        }

        return hash;
    }

    // Getters

    @Override
    public Stream<Click> clicks() {
        return this.clicks.stream()
                .dropWhile(r -> r.dateTime().isBefore(start))
                .takeWhile(r -> r.dateTime().isBefore(end));

    }

    @Override
    public Stream<Impression> impressions() {
        return this.impressions.stream()
                .dropWhile(r -> r.dateTime().isBefore(start))
                .takeWhile(r -> r.dateTime().isBefore(end))
                .filter(i -> impFilters.keySet().stream().filter(filterActive::get)
                        .allMatch(f -> impFilters.get(f).test(i)));
    }

    @Override
    public Stream<Server> server() {
        return this.server.stream()
                .dropWhile(r -> r.dateTime().isBefore(start))
                .takeWhile(r -> r.dateTime().isBefore(end));
    }

}
