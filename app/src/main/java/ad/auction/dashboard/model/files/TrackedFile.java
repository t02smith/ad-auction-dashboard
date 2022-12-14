package ad.auction.dashboard.model.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.model.files.records.SharedFields;

/**
 * Represents a file that is to be read from
 * 
 * @author tcs1g20
 */
public class TrackedFile implements Callable<List<SharedFields>> {

    private static final Logger logger = LogManager.getLogger(TrackedFile.class.getSimpleName());

    //File location
    private final String filename;

    //File type: IMPRESSION, CLICK, SERVER
    private final FileType type;

    //Records of invalid format
    private int invalidRecords = 0;

    /**
     * Create a new tracked file
     * @param filename the file's location
     */
    public TrackedFile(String filename) {
        this.filename = filename;
        this.type = this.determineFileType();
    }

    /**
     * Determines whether a file is of type impression, 
     * server or click
     * @return the determined filetype
     */
    public FileType determineFileType() {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(this.filename));

            String[] headers = reader.readLine().split(",");
            reader.close();

            var type = FileType.determineType(headers);
            logger.info("File {} is type {}", filename, type);
            return type;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
        
    }

    /**
     * Read the data from the log file
     * @return the data records read from the file
     */
    @Override
    public List<SharedFields> call() {
        long time = System.currentTimeMillis();
        logger.info("Attempting to read file '{}:{}'", type, filename);

        CsvParserSettings settings = new CsvParserSettings();
        settings.setHeaders(type.getHeaders());
        settings.setMaxColumns(type.getHeaders().length);
        settings.setHeaderExtractionEnabled(true);
        settings.setLineSeparatorDetectionEnabled(true);
        
        CsvParser parser = new CsvParser(settings);
        List<SharedFields> records = new ArrayList<>();

        parser.beginParsing(new File(filename));


        String[] ln;
        while ((ln = parser.parseNext()) != null) {
            try {
                records.add(type.produce(ln));
            } catch (IllegalArgumentException e) {
                invalidRecords++;
            }
        }

        logger.info("{} parsed in {}ms", filename, System.currentTimeMillis()-time);
        if (invalidRecords > 0) logger.warn("{} records were invalid in {}", invalidRecords, filename);
        
        return records;

    }

    /**
     * @return the file's type
     */
    public FileType getType() {
        return this.type;
    }

    /**
     * Get the number of invalid records
     * @return number of invalid records
     */
    public int getInvalidRecords() {
        return this.invalidRecords;
    }
}
