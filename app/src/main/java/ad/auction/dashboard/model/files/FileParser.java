package ad.auction.dashboard.model.files;

import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.files.records.SharedFields;

/**
 * This class will connect to the filestream
 * and process the data into records
 * 
 * @author tcs1g20
 */
public class FileParser implements Callable<List<SharedFields>> {

    private static final Logger logger = LogManager.getLogger(FileParser.class.getSimpleName());

    private final PipedInputStream pipe;
    private final FileType type;

    public FileParser(PipedInputStream pipe, FileType type) {
        this.pipe = pipe;
        this.type = type;
    }
    
    @Override
    public List<SharedFields> call() throws Exception {
        final ArrayList<SharedFields> objs = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        long startTime = System.currentTimeMillis();

        int c;
        while ((c = pipe.read()) != 255) {
            if ((char)c == '\n') {
                objs.add(type.produce(builder.toString()));
                builder.setLength(0);
            } else builder.append((char)c);
        }
        
        pipe.close();
        logger.info("File processed in {}ms", System.currentTimeMillis()-startTime);
        return objs;        
    }
}
