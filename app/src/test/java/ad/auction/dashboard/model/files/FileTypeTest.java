package ad.auction.dashboard.model.files;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class FileTypeTest {

    //Identifying file types
 
    @Test
    @DisplayName("Correct type: Impression")
    @Tag("model/files")
    public void correctTypeImpression() {
        TrackedFile tf = new TrackedFile("./data/impression_log.csv");
        assertEquals(FileType.IMPRESSION, tf.getType());
    }

    @Test
    @DisplayName("Correct type: Click")
    @Tag("model/files")
    public void correctTypeClick() {
        TrackedFile tf = new TrackedFile("./data/click_log.csv");
        assertEquals(FileType.CLICK, tf.getType());
    }

    @Test
    @DisplayName("Correct type: Server")
    @Tag("model/files")
    public void correctTypeServer() {
        TrackedFile tf = new TrackedFile("./data/server_log.csv");
        assertEquals(FileType.SERVER, tf.getType());
    }

    //TODO add tests for data after it's read and cast
    
}
