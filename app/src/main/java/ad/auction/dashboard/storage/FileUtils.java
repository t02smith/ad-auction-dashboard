package ad.auction.dashboard.storage;

import java.io.File;

public class FileUtils {
    // get folder/filename from a path string
    public static String toFileName(String filepath) {
        File file = new File(filepath);
        return file.getName();
    }
}
