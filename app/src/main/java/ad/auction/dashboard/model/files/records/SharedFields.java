package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

/**
 * Getters for shared fields
 * Allows generalisation when handling dates/ids
 */
public interface SharedFields {
    
    public LocalDateTime dateTime();
    public long ID();
}
