package ad.auction.dashboard.model.files;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents a file that is to be read from
 * 
 * @author tcs1g20
 */
public class TrackedFile implements Runnable {

    private static final Logger logger = LogManager.getLogger(TrackedFile.class.getSimpleName());

    //File location
    private final String filename;

    //Output stream
    private final PipedOutputStream pipe = new PipedOutputStream();

    private final FileType type;

    public TrackedFile(String filename) {
        this.filename = filename;
        this.type = this.determineFileType();
    }

    private FileType determineFileType() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(this.filename));

            String[] headers = reader.readLine().split(",");
            reader.close();

            return FileType.determineType(headers);

            
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return FileType.OTHER;
        
    }

    /**
     * Reads a file line by line and sends it through the pipe
     */
    @Override
    public void run() {
        long time = System.nanoTime();
        logger.info("Attempting to read file '{}'", filename);
        
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(this.filename));
            if (this.type != FileType.OTHER) reader.readLine();

            byte[] newline = "\n".getBytes();

            String msg;
            logger.info("Reading file '{}'", filename);
            while ((msg = reader.readLine()) != null) {
                pipe.write(msg.getBytes());
                pipe.write(newline);
            }

            pipe.write(-1);

            logger.info("Finished reading '{}'", filename);
            reader.close();

        } catch (FileNotFoundException e) {
            logger.error("File '{}' not found", filename);
        } catch (IOException e) {
            logger.error("Error reading '{}': {}", filename, e.getMessage());
        }

        long timeEnd = System.nanoTime();
        logger.info("Read '{}' in {}ms", filename, (timeEnd-time)/1000000);
        
    }

    /**
     * Connect to the output stream
     * @param p an input stream
     */
    public void connect(PipedInputStream p) {
        try {
            this.pipe.connect(p);
        } catch (IOException e) {
            logger.error("Unable to connect to '{}'s pipe",filename);
        }
    }

    /**
     * Close the pipe
     */
    public void close() {
        try {
            this.pipe.close();
        } catch (IOException e) {
            logger.error("Error closing pipe");
        }
    }

    public FileType getType() {
        return this.type;
    }

}
