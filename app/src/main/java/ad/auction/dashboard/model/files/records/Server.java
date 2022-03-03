package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import ad.auction.dashboard.model.Utility;

public record Server(LocalDateTime dateTime, long ID, LocalDateTime exitDate, int pagesViewed, boolean conversion) implements SharedFields {

    /**
     * produce a new server
     * @param line one line in the file
     * @return constructed server
     */
    public static Server producer(String line) {
        String[] parts = line.split(",");

        return new Server(
            Utility.parseDate(parts[0]),    //Date
            Long.parseLong(parts[1]),       //ID
            parts[2].equals("n/a") ? LocalDateTime.MAX:Utility.parseDate(parts[2]),    //Exit date
            Integer.parseInt(parts[3]),     //Pages viewed
            parts[4].equals("Yes")          //Conversion
        );
    }

    public long secondsOnWebsite() {
        return Math.abs(ChronoUnit.SECONDS.between(dateTime, exitDate));
    }
}
