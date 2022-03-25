package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import ad.auction.dashboard.model.Utility;

public record Server(LocalDateTime dateTime, long ID, LocalDateTime exitDate, int pagesViewed, boolean conversion)
        implements SharedFields {

    public static Server producer(String[] line) {
        int pv = Integer.parseInt(line[3]);
        if (pv < 0) throw new IllegalArgumentException("Pages viewed must be at least 0");

        var start = Utility.parseDate(line[0]);
        var end = line[2].equals("n/a") ? LocalDateTime.MAX : Utility.parseDate(line[2]);
        if (start.isAfter(end)) throw new IllegalArgumentException("Start date must be before end date");

        return new Server(
                start, // Date
                Long.parseLong(line[1]), // ID
                end, // Exit date
                pv, // Pages viewed
                line[4].equals("Yes") // Conversion
        );
    }

    public long secondsOnWebsite() {
        return Math.abs(ChronoUnit.SECONDS.between(dateTime, exitDate));
    }
}
