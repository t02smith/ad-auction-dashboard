package ad.auction.dashboard.model.files;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.TestUtility;

public class FileTypeTest {

    //Identifying file types
 
    @Test
    @DisplayName("Correct type: Impression")
    @Tag("model/files")
    public void correctTypeImpression() {
        var file = TestUtility.getResourceFile("/data/2-week/impression_log.csv");

        TrackedFile tf = new TrackedFile(file);
        assertEquals(FileType.IMPRESSION, tf.getType());
    }

    @Test
    @DisplayName("Correct type: Click")
    @Tag("model/files")
    public void correctTypeClick() {
        var file = TestUtility.getResourceFile("/data/2-week/click_log.csv");

        TrackedFile tf = new TrackedFile(file);
        assertEquals(FileType.CLICK, tf.getType());
    }

    @Test
    @DisplayName("Correct type: Server")
    @Tag("model/files")
    public void correctTypeServer() {
        var file = TestUtility.getResourceFile("/data/2-week/server_log.csv");

        TrackedFile tf = new TrackedFile(file);
        assertEquals(FileType.SERVER, tf.getType());
    }

    //TODO add tests for data after it's read and cast
    
}
