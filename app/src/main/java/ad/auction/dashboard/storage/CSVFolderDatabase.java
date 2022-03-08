package ad.auction.dashboard.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Csv folder storage class, used to store loaded folders in a text file
 */
public class CSVFolderDatabase {
    // the folders uploaded to the database
    private List<String> folders = new ArrayList<>();

    public CSVFolderDatabase() {
        // load from file when the database class created
        loadFromfile();
    }

    // return loaded folders
    public List<String> getFolders() {
        return folders;
    }

    // add a record to databse
    public void addFolder(String folderPath) {
        if (!folders.contains(folderPath)) {
            folders.add(folderPath);
        }
    }

    public void loadFromfile() {
        try {
            // clear previous results
            folders.clear();

            Scanner scanner = new Scanner(new File("database.txt"));
            // read each line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                line = line.trim();
                if (line.length() != 0) {
                    // add to folders
                    folders.add(line);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
        }
    }

    public void saveToFile() {
        try {
            // open file
            PrintWriter printWriter = new PrintWriter(new File("database.txt"));
            for (String path : folders) {
                // write to file
                printWriter.println(path);
            }
            // close
            printWriter.flush();
            printWriter.close();
        } catch (FileNotFoundException e) {
        }
    }
}
