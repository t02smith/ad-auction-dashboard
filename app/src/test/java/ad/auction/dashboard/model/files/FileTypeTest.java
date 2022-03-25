package ad.auction.dashboard.model.files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("model/files")
public class FileTypeTest {

    //Identifying file types
 
    @Test
    @DisplayName("Correct type: Impression")
    public void correctTypeImpression() {
        TrackedFile tf = new TrackedFile("./data/2-week/impression_log.csv");
        assertEquals(FileType.IMPRESSION, tf.getType());
    }

    @Test
    @DisplayName("Correct type: Click")
    public void correctTypeClick() {
        TrackedFile tf = new TrackedFile("./data/2-week/click_log.csv");
        assertEquals(FileType.CLICK, tf.getType());
    }

    @Test
    @DisplayName("Correct type: Server")
    public void correctTypeServer() {
        TrackedFile tf = new TrackedFile("./data/2-week/server_log.csv");
        assertEquals(FileType.SERVER, tf.getType());
    }

    @Test
    @DisplayName("Invalid type")
    public void invalidTypeTest() {
        var tf = new TrackedFile("./data/invalid_headers.csv");
        assertNull(tf.getType());
    }
    
}
