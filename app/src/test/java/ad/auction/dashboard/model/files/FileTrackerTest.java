package ad.auction.dashboard.model.files;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.TestUtility;

public class FileTrackerTest {

    private FileTracker fileTracker;


    @BeforeEach
    public void setup() {
        this.fileTracker = new FileTracker();
    }

    @AfterEach
    public void tearDown() {
        this.fileTracker = null;
    }
    
    @Test
    @DisplayName("File tracked successfully")
    @Tag("model/files")
    public void isFileTrackedTest() {

        var filename = TestUtility.getResourceFile("/data/test-1.txt");
        if (filename == null) fail();

        fileTracker.trackFile(filename);

        assertTrue(
            fileTracker.isFileTracked(filename),
            "File not tracked by filetracker"
        );
        

    }

    @Test
    @DisplayName("File track and untrack successfully")
    @Tag("model/files")
    public void fileTrackUntrackTest() {
        var filename = TestUtility.getResourceFile("/data/test-1.txt");
        if (filename == null) fail();

        fileTracker.trackFile(filename);

        if (!fileTracker.isFileTracked(filename)) {
            fail("Failed to track file");
        }

        fileTracker.untrackFile(filename);

        assertFalse(
            fileTracker.isFileTracked(filename),
            "File wasn't untracked"
        );
    }

}
