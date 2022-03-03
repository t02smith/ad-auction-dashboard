package ad.auction.dashboard.model.files;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Function;

import ad.auction.dashboard.model.files.records.Click;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Server;
import ad.auction.dashboard.model.files.records.SharedFields;

/**
 * Lists all possible file types
 */
public enum FileType {
    IMPRESSION(line -> Impression.producer(line), "Date", "ID", "Gender", "Age", "Income", "Context", "Impression Cost"),
    CLICK(line -> Click.producer(line), "Date", "ID", "Click Cost"),
    SERVER(line -> Server.producer(line), "Entry Date", "ID", "Exit Date", "Pages Viewed", "Conversion"),
    OTHER(line -> null);

    //Produces a record from a csv line
    private final Function<String, ? extends SharedFields> producer;

    //List of column headers in .csv file
    private final String[] headers;

    /**
     * Each file type
     * @param producer
     * @param headers
     */
    private FileType(Function<String, ? extends SharedFields> producer, String... headers) {
        this.producer = producer;
        this.headers = headers;
    }

    /**
     * Apply producer function
     * @param line
     * @return
     */
    public SharedFields produce(String line) {
        return this.producer.apply(line);
    }

    public String[] getHeaders() {
        return this.headers;
    }

    public static FileType determineType(String[] headers) {
        HashSet<String> actual = new HashSet<>(Arrays.asList(headers));

        for (FileType type: FileType.values()) {
            HashSet<String> given = new HashSet<>(Arrays.asList(type.getHeaders()));
            if (given.equals(actual)) return type;
            
        }

        throw new IllegalArgumentException("Unexpected headers");
    }
}
