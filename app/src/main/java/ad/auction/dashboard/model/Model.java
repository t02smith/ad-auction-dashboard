package ad.auction.dashboard.model;

import java.util.Optional;

import ad.auction.dashboard.model.files.FileTracker;
import ad.auction.dashboard.model.files.FileTracker.FileTrackerQuery;

// TODO model querying

public class Model {
    
    private final FileTracker fileTracker = new FileTracker();

    /**
     * Allows the controller to prompt the model to perform file actions
     * @param query The chosen command
     * @param target The target file
     * @return The result if any
     */
    public Optional<?> queryFileTracker(FileTrackerQuery query, String filename) {
        return this.fileTracker.query(query, filename);
    }
}
