package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

/**
 * Shared fields between all of the record types
 * This interface streamlines calculations where the record 
 * type is not important
 * 
 * @author tcs1g20
 */
public interface SharedFields {
    public long ID();
    public LocalDateTime dateTime();
}
