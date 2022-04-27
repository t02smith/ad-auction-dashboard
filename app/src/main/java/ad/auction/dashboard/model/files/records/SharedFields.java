package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

/**
 * Getters for shared fields
 * Allows generalisation when handling dates/ids
 *
 * @author tcs1g20
 */
public interface SharedFields {
    
    LocalDateTime dateTime();
    long ID();
}
