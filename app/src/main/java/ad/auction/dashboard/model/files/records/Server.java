package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

import ad.auction.dashboard.model.Utility;

public record Server(LocalDateTime dateTime, String ID, LocalDateTime exitDate, int pagesViewed, boolean conversion) {

    /**
     * produce a new server
     * @param line one line in the file
     * @return constructed server
     */
    public static Server producer(String line) {
        String[] parts = line.split(",");

        return new Server(
            Utility.parseDate(parts[0]), 
            parts[1], 
            Utility.parseDate(parts[2]), 
            Integer.parseInt(parts[3]), 
            parts[4].equals("Yes")
        );
    }
}
