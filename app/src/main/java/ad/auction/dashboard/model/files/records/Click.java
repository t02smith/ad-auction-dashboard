package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import ad.auction.dashboard.model.Util;

/**
 * A data record from the click log file
 * @param dateTime The date & time of the click
 * @param ID The user's uid
 * @param clickCost The cost of this specific click
 */
public record Click(LocalDateTime dateTime, long ID, float clickCost) implements SharedFields {

    /**
     * Produces a Click record based off a line from the log file
     * @param line line from click log file
     * @return the line's data record
     * @throws IllegalArgumentException incorrect line format
     */
    public static Click producer(String[] line) throws IllegalArgumentException {

        try {
            float cc = Float.parseFloat(line[2]);
            if (cc < 0) throw new IllegalArgumentException("Click cost must be at least 0");

            long id = Long.parseLong(line[1]);
            if (id < 0) throw new IllegalArgumentException("ID must be a positive value");

            return new Click(Util.parseDate(line[0]),id,cc);

        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IllegalArgumentException("Incorrect field format");
        }

    }



}