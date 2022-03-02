package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

import ad.auction.dashboard.model.Utility;

public record Click(LocalDateTime dateTime, long ID, float clickCost){

    /**
     * produce a new Click
     * @param line one line in the file
     * @return constructed Click
     */
    public static Click producer(String line) {
        String[] parts = line.split(",");
        return new Click(
            Utility.parseDate(parts[0]),    //Date
            Long.parseLong(parts[1]),       //ID
            Float.parseFloat(parts[2])      //Click costs
        );
    }

}