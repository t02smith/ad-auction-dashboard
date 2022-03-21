package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

import ad.auction.dashboard.model.Utility;

public record Click(LocalDateTime dateTime, long ID, float clickCost) implements SharedFields {

    public static Click producer(String[] line) {
        return new Click(
            Utility.parseDate(line[0]),    //Date
            Long.parseLong(line[1]),       //ID
            Float.parseFloat(line[2])      //Click costs
        );
    }

}