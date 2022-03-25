package ad.auction.dashboard.model.files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.TestUtility;

import static org.junit.jupiter.api.Assertions.*;

@Tag("model/files")
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

    @Test
    @DisplayName("Empty file test")
    public void emptyFileTest() throws Exception {
        var filename = "./data/empty_impression.csv";

        fileTracker.trackFile(filename);
        var res = fileTracker.readFile(filename);

        while (!res.isDone()) {}

        assertEquals(0, res.get().size());
    }

}
