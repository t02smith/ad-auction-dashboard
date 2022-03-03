package ad.auction.dashboard.model.files.records;

import java.util.stream.Stream;

import ad.auction.dashboard.model.files.FileType;

/**
 * A bundle is a wrapper containing a stream and its 
 * corresponding file type
 * 
 * This is to help the calculator recognise what calculations
 * are possible.
 * 
 * @author tcs1g20
 */
public record Bundle(Stream<? extends SharedFields> stream, FileType type) {
    
}
