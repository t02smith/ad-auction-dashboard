package ad.auction.dashboard.model.files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import ad.auction.dashboard.TestUtility;
import ad.auction.dashboard.model.files.FileTracker.FileTrackerQuery;

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

        fileTracker.query(
            FileTrackerQuery.TRACK,
            filename
        );

        assertTrue(
            (boolean)fileTracker.query(FileTrackerQuery.IS_TRACKED, filename).get(),
            "File not tracked by filetracker"
        );
        

    }

    @Test
    @DisplayName("File track and untrack successfully")
    @Tag("model/files")
    public void fileTrackUntrackTest() {
        var filename = TestUtility.getResourceFile("/data/test-1.txt");
        if (filename == null) fail();

        fileTracker.query(
            FileTrackerQuery.TRACK, 
            filename
        );

        if (!(boolean)fileTracker.query(FileTrackerQuery.IS_TRACKED, filename).get()) {
            fail("Failed to track file");
        }

        fileTracker.query(
            FileTrackerQuery.UNTRACK, 
            filename
        );

        assertFalse(
            (boolean)fileTracker.query(FileTrackerQuery.IS_TRACKED, filename).get(),
            "File wasn't untracked"
        );
    }

    @Test
    @DisplayName("Successful file read")
    @Tag("model/files")
    @Timeout(10)
    @SuppressWarnings("unchecked")
    public void fileCorrectRead() {

        List<String> expectedContent = Arrays.asList(new String[] {
            "this is a test file",
            "written for the purpose of testing"
        });

        var filename = TestUtility.getResourceFile("/data/test-1.txt");
        if (filename == null) fail();

        fileTracker.query(
            FileTrackerQuery.TRACK, 
            filename
        );

        Stream<Object> read = (Stream<Object>)fileTracker.query(
            FileTrackerQuery.READ, 
            filename
        ).get();

        List<String> actual = read.map(line -> (String)line)
                                  .toList();

        assertEquals(expectedContent, actual);
    }
}
