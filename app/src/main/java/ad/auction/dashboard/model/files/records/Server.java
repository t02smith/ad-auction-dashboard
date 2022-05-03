package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import ad.auction.dashboard.model.Util;

/**
 * A data record from the server log
 * @param dateTime the date & time the user entered the website
 * @param ID the user's uid
 * @param exitDate the date & time the user exits the website
 * @param pagesViewed how many pages the user viewed
 * @param conversion whether a conversion occurred
 */
public record Server(LocalDateTime dateTime, long ID, LocalDateTime exitDate, int pagesViewed, boolean conversion)
        implements SharedFields {

    /**
     * Produces a server data record from a line in the server log file
     * @param line the line in the server log file
     * @return the generated Server record
     */
    public static Server producer(String[] line) {

        try {
            int pv = Integer.parseInt(line[3]);
            if (pv < 0) throw new IllegalArgumentException("Pages viewed must be at least 0");

            var start = Util.parseDate(line[0]);
            var end = line[2].equals("n/a") ? LocalDateTime.MAX : Util.parseDate(line[2]);
            if (start.isAfter(end)) throw new IllegalArgumentException("Start date must be before end date");

            boolean conversion;
            if (line[4].equals("Yes")) conversion = true;
            else if (line[4].equals("No")) conversion = false;
            else throw new IllegalArgumentException("Conversion must be yes/no");

            return new Server(
                    start, // Date
                    Long.parseLong(line[1]), // ID
                    end, // Exit date
                    pv, // Pages viewed
                    conversion // Conversion
            );
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid server log record");
        }
    }

    /**
     * @return how many seconds the user spent on the website
     */
    public long secondsOnWebsite() {
        return Math.abs(ChronoUnit.SECONDS.between(dateTime, exitDate));
    }
}
