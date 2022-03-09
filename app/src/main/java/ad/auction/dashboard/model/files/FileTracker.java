package ad.auction.dashboard.model.files;

import java.io.IOException;
import java.io.PipedInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


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
     * Types of query to the FileTracker
     */
    public enum FileTrackerQuery {
        TRACK,
        UNTRACK,
        IS_TRACKED,
        READ;
    }

    /**
     * Allows the model to interface with this class via a query
     * @param query The type of action
     * @param filename The target file
     * @return the output of the query if there is one
     */
    public Optional<Object> query(FileTrackerQuery query, String filename) {
        logger.info("Performing {} on '{}'", query, filename);

        switch (query) {
            case TRACK:
                this.trackFile(filename);
                return Optional.empty();
            case READ:
                try {return Optional.of(this.readFile(filename));}
                catch (IOException e) {
                    logger.error("Error executing READ query: {}", e.getMessage());
                    return Optional.empty();
                }
            case IS_TRACKED:
                return Optional.of(this.isFileTracked(filename));
            case UNTRACK:
                this.untrackFile(filename);
                return Optional.empty();
        }

        return Optional.empty();
    }

    /**
     * Start tracking a file but don't read it
     * @param filename the location of the file
     */
    private void trackFile(String filename) {
        synchronized (this.trackedFiles) {
            logger.info("Tracking file '{}'", filename);
            this.trackedFiles.put(filename, new TrackedFile(filename));
        }
    }

    /**
     * Will trigger and read from the pipe and cast each line to
     * its corresponding record type.
     * @param filename The file location/id
     * @return A stream of all the records
     * @throws IOException
     */
    private Future<List<Object>> readFile(String filename) throws IOException  {
        TrackedFile file;

        synchronized (this.trackedFiles) {
            if (!this.isFileTracked(filename)) return null;
            file = trackedFiles.get(filename);
        }
        
        FileType type = file.getType();

        final PipedInputStream pipe = new PipedInputStream();

        file.reloadPipe();
        file.connect(pipe);

        //Start reading the file
        executor.submit(file);

       

        //Start parsing the file
        return executor.submit(new FileParser(pipe, type));  
    }

    /**
     * Remove a file from a tracked list
     * @param filename
     */
    private void untrackFile(String filename) {
        synchronized (this.trackedFiles) {
            if (!isFileTracked(filename)) return;

            this.trackedFiles.get(filename).close();
            this.trackedFiles.remove(filename);
    
            logger.info("File '{}' untracked", filename);
        }
    }

    /**
     * Checking whether a file is being tracked
     * @param filename
     * @return
     */
    private boolean isFileTracked(String filename) {
        synchronized(this.trackedFiles) {
            if (!trackedFiles.containsKey(filename)) {
                logger.error("File '{}' not being tracked");
                return false;
            }
    
            return true;
        }        
    }
}
