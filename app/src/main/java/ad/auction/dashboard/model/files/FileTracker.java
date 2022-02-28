package ad.auction.dashboard.model.files;

import java.io.IOException;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;


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

    //Files being tracked
    private final HashMap<String, TrackedFile> trackedFiles = new HashMap<>();

    public static void main(String[] args) throws Exception {
        String file = args[0];

        FileTracker ft = new FileTracker();
        ft.trackFile(file);

        ft.readFile(file);
    }

    /**
     * Start tracking a file but don't read it
     * @param filename the location of the file
     */
    public void trackFile(String filename) {
        logger.info("Tracking file '{}'", filename);
        this.trackedFiles.put(filename, new TrackedFile(filename));
    }

    /**
     * Will trigger and read from the pipe and cast each line to
     * its corresponding record type.
     * @param filename The file location/id
     * @return A stream of all the records
     * @throws IOException
     */
    public Stream<Object> readFile(String filename) throws IOException  {

        //Assert the file is tracked
        if (!trackedFiles.containsKey(filename)) {
            logger.error("File '{}' not being tracked");
            return null;
        }

        TrackedFile file = trackedFiles.get(filename);

        final PipedInputStream pipe = new PipedInputStream();
        file.connect(pipe);

        //Start reading the file
        new Thread(file)
            .start();


        final ArrayList<Object> objs = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        int c;
        while ((c = pipe.read()) != 255) {
            if ((char)c == '\n') {
                //parse line
                //logger.info(builder.toString());
                builder.setLength(0);
            } else builder.append((char)c);
        }
        
        pipe.close();
        return objs.stream();        
    }
}
