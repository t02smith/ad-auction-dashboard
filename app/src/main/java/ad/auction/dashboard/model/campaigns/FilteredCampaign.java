package ad.auction.dashboard.model.campaigns;

import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;
import ad.auction.dashboard.model.files.records.SharedFields;
import org.jetbrains.annotations.NotNull;

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

    //Filters to be applied to all datasets and just the impressions
    private final HashMap<Integer, Predicate<SharedFields>> allFilters = new HashMap<>();
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
     * Add a filter to all datasets
     * e.g. date
     * 
     * @param filter the filter function
     * @return the hash identifier
     */
    public int addFilter(@NotNull Predicate<SharedFields> filter) {
        int hash = filter.hashCode();
        if (!allFilters.containsKey(hash)) {
            this.allFilters.put(hash, filter);
            this.filterActive.put(hash, true);
        }

        return hash;
    }

    /**
     * Add a filter to just the impressions data
     * e.g. Gender,
     * @param filter
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
                .filter(i -> allFilters.keySet().stream().filter(filterActive::get)
                        .allMatch(f -> allFilters.get(f).test(i)));

    }

    @Override
    public Stream<Impression> impressions() {
        return this.impressions.stream()
                .filter(i -> allFilters.keySet().stream().filter(filterActive::get)
                        .allMatch(f -> allFilters.get(f).test(i)))
                .filter(i -> impFilters.keySet().stream().filter(filterActive::get)
                        .allMatch(f -> impFilters.get(f).test(i)));
    }

    @Override
    public Stream<Server> server() {
        return this.server.stream()
                .filter(i -> allFilters.keySet().stream().filter(filterActive::get)
                        .allMatch(f -> allFilters.get(f).test(i)));
    }

}
