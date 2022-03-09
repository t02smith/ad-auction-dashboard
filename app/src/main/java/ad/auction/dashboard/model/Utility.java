package ad.auction.dashboard.model;

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

    /**
     * Rounds a number to n decimal places
     * @param number
     * @param n
     * @return
     */
    public static double roundNDp(double number, int n) {
        final double factor = Math.pow(10, n);
        return (double)Math.round(number*factor)/factor;
    }
    
}
