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


    public static void readFile(FileTracker tracker, String relativeFilename) {
        var filename = TestUtility.getResourceFile(relativeFilename);
        if (filename == null) fail();

        tracker.trackFile(filename);
        var res = tracker.readFile(filename);
        while (!res.isDone()) {}
    }

    @Test
    @DisplayName("File tracked successfully")
    public void isFileTrackedTest() {
        var filename = TestUtility.getResourceFile("/data/log_files/impression_log.csv");
        if (filename == null) fail();

        fileTracker.trackFile(filename);

        assertTrue(
            fileTracker.isFileTracked(filename),
            "File not tracked by filetracker"
        );
        

    }

    @Test
    @DisplayName("File tracked and untracked successfully")
    public void fileTrackUntrackedTest() {
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
        var filename = TestUtility.getResourceFile("/data/log_files/empty_impression.csv");

        fileTracker.trackFile(filename);
        var res = fileTracker.readFile(filename);
        while (!res.isDone()) {}

        assertEquals(0, res.get().size());
    }

    @Test
    @DisplayName("No invalid records")
    public void noInvalidRecordsTest() {
        var filename = TestUtility.getResourceFile("/data/log_files/empty_impression.csv");

        fileTracker.trackFile(filename);
        var res = fileTracker.readFile(filename);
        while (!res.isDone()) {}

        assertEquals(0, fileTracker.getFile(filename).getInvalidRecords());
    }

    @Test
    @DisplayName("Many invalid records - click")
    public void manyInvalidRecordsClickTest() {
        var filename = TestUtility.getResourceFile("/data/log_files/invalid/invalid_click.csv");

        fileTracker.trackFile(filename);
        var res = fileTracker.readFile(filename);
        while (!res.isDone()) {}

        assertEquals(3, fileTracker.getFile(filename).getInvalidRecords());
    }

    @Test
    @DisplayName("Many invalid records - impression")
    public void manyInvalidRecordsImpressionTest() {
        var filename = TestUtility.getResourceFile("/data/log_files/invalid/invalid_impressions.csv");

        fileTracker.trackFile(filename);
        var res = fileTracker.readFile(filename);
        while (!res.isDone()) {}

        assertEquals(6, fileTracker.getFile(filename).getInvalidRecords());
    }

    @Test
    @DisplayName("Many invalid records - server")
    public void manyInvalidRecordsServerTest() {
        var filename = TestUtility.getResourceFile("/data/log_files/invalid/invalid_server.csv");

        fileTracker.trackFile(filename);
        var res = fileTracker.readFile(filename);
        while (!res.isDone()) {}

        assertEquals(5, fileTracker.getFile(filename).getInvalidRecords());
    }

}
