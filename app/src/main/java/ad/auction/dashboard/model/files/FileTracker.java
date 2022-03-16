package ad.auction.dashboard.model.files;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.files.records.SharedFields;


/**
 * Manages all the files being tracked by the system
 * Provides interface for Model to interact with files
 * 
 * @author tcs1g20
 */
public class FileTracker {

    private static final Logger logger = LogManager.getLogger(FileTracker.class.getSimpleName());

    //Max number of files to be processed at once
    private static final int FILE_TRACKER_THREAD_COUNT = 10;

    //Files being tracked
    private final HashMap<String, TrackedFile> trackedFiles = new HashMap<>();

    private final ExecutorService executor = Executors.newFixedThreadPool(FILE_TRACKER_THREAD_COUNT);

    /**
     * Start tracking a file but don't read it
     * @param filename the location of the file
     */
    public void trackFile(String filename) {
        synchronized (this.trackedFiles) {
            logger.info("Tracking file '{}'", filename);
            this.trackedFiles.put(filename, new TrackedFile(filename));
        }
    }

    public boolean correctFileType(String filename, FileType expected) {
        synchronized (this.trackedFiles) {
            return this.trackedFiles.get(filename).getType() == expected;
        }
    }

    /**
     * Will trigger and read from the pipe and cast each line to
     * its corresponding record type.
     * @param filename The file location/id
     * @return A stream of all the records
     */
    public Future<List<SharedFields>> readFile(String filename) {
        TrackedFile file;

        synchronized (this.trackedFiles) {
            if (!this.isFileTracked(filename)) return null;
            file = trackedFiles.get(filename);
        }

        //Start reading the file
        return executor.submit(file);

    }

    /**
     * Remove a file from a tracked list
     * @param filename name of file
     */
    public void untrackFile(String filename) {
        synchronized (this.trackedFiles) {
            if (!isFileTracked(filename)) return;
            this.trackedFiles.remove(filename);
    
            logger.info("File '{}' untracked", filename);
        }
    }

    /**
     * Checking whether a file is being tracked
     * @param filename name of file to track
     * @return whether the file has been tracked
     */
    public boolean isFileTracked(String filename) {
        synchronized(this.trackedFiles) {
            if (!trackedFiles.containsKey(filename)) {
                logger.error("File '{}' not being tracked", filename);
                return false;
            }
    
            return true;
        }        
    }

    /**
     * Takes a set of expected files and removes any not mentioned
     * @param files expected files
     */
    public void clean(Set<String> files) {
        logger.info("Cleaning files");
        trackedFiles.keySet().forEach(f -> {
            if (!files.contains(f)) {
                this.untrackFile(f);
            }
        });
        logger.info("Files cleaned");
    }
}
