package ad.auction.dashboard.model;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility functions for all model classes
 * @author tcs1g20
 */
public final class Utility {
    
    //Format given for dates in input files
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Parse a date in the specified format
     * @param date
     * @return
     */
    public static LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date, Utility.formatter);
    }

    public static String getResourceFile(String target) {
        try {
            return Utility.class
                .getResource(target)
                .toURI()
                .getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
    
}
