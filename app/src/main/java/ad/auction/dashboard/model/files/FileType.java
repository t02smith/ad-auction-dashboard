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
 *
 * @author tcs1g20
 */
public enum FileType {
    IMPRESSION(Impression::producer, "Date", "ID", "Gender", "Age", "Income", "Context", "Impression Cost"),
    CLICK(Click::producer, "Date", "ID", "Click Cost"),
    SERVER(Server::producer, "Entry Date", "ID", "Exit Date", "Pages Viewed", "Conversion");

    //Produces a record from a csv line
    private final Function<String[], SharedFields> producer;

    //List of column headers in .csv file
    private final String[] headers;

    /**
     * Each file type
     * @param producer The function to produce the type record
     * @param headers The expected headings in the .csv
     */
    FileType(Function<String[], SharedFields> producer, String... headers) {
        this.producer = producer;
        this.headers = headers;
    }

    /**
     * Runs the FileType producer on a line from the log file
     * @param line the log line
     * @return the generated data record
     */
    public SharedFields produce(String[] line) {
        return this.producer.apply(line);
    }

    /**
     * @return The expected headers for the file
     */
    public String[] getHeaders() {
        return this.headers;
    }

    /**
     * Determine a log file's type given its column headers
     * @param headers the file's column headers
     * @return the log file's type
     */
    public static FileType determineType(String[] headers) {
        HashSet<String> actual = new HashSet<>(Arrays.asList(headers));

        for (FileType type: FileType.values()) {
            HashSet<String> given = new HashSet<>(Arrays.asList(type.getHeaders()));
            if (given.equals(actual)) return type;
            
        }

        throw new IllegalArgumentException("Unexpected headers");
    }
}
