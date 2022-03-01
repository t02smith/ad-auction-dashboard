package ad.auction.dashboard.model.files;

import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Impression;

/**
 * Lists all possible file types
 */
public enum FileType {
    IMPRESSION(line -> Impression.producer(line), "Date", "ID", "..."),
    CLICK(line -> null),
    SERVER(line -> null);

    //Produces a record from a csv line
    private final Function<String, ?> producer;

    //List of column headers in .csv file
    private final String[] headers;

    /**
     * Each file type
     * @param producer
     * @param headers
     */
    private FileType(Function<String, ?> producer, String... headers) {
        this.producer = producer;
        this.headers = headers;
    }

    /**
     * Apply producer function
     * @param line
     * @return
     */
    public Object produce(String line) {
        return this.producer.apply(line);
    }

    public String[] getHeaders() {
        return this.headers;
    }
}
