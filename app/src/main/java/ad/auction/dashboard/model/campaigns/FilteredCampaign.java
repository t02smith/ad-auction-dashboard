package ad.auction.dashboard.model.campaigns;

import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;
import ad.auction.dashboard.model.files.records.SharedFields;

/**
 * A filtered campaign applies filters to the data before returning it
 * Filters are identified by their hashCode.
 * 
 * The view will specify what the filter is initially and then reference
 * it using the hashcode
 */
public class FilteredCampaign extends Campaign {

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
     * 
     * @param filterHash
     */
    public void toggleFilter(int filterHash) {
        if (filterActive.containsKey(filterHash))
            filterActive.replace(filterHash, !filterActive.get(filterHash));
    }

    /**
     * Add a filter to just the impressions data
     * e.g. Gender,
     * 
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

    /**
     * Add a filter to all datasets
     * e.g. date
     * 
     * @param filter
     * @return
     */
    public int addFilter(Predicate<SharedFields> filter) {
        int hash = filter.hashCode();
        if (!allFilters.containsKey(hash)) {
            this.allFilters.put(hash, filter);
            this.filterActive.put(hash, false);
        }

        return hash;
    }

    // Getters

    @Override
    public Stream<Click> clicks() {
        return this.clicks.stream()
                .filter(i -> allFilters.keySet().stream().filter(f -> filterActive.get(f))
                        .allMatch(f -> allFilters.get(f).test(i)));

    }

    @Override
    public Stream<Impression> impressions() {
        return this.impressions.stream()
                .filter(i -> allFilters.keySet().stream().filter(f -> filterActive.get(f))
                        .allMatch(f -> allFilters.get(f).test(i)))
                .filter(i -> impFilters.keySet().stream().filter(f -> filterActive.get(f))
                        .allMatch(f -> impFilters.get(f).test(i)));
    }

    @Override
    public Stream<Server> server() {
        return this.server.stream()
                .filter(i -> allFilters.keySet().stream().filter(f -> filterActive.get(f))
                        .allMatch(f -> allFilters.get(f).test(i)));
    }

}
