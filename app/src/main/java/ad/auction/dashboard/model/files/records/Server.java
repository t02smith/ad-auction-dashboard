package ad.auction.dashboard.model.files.records;

public record Server(String dateTime, String ID, String exitDate, int pagesViewed, boolean conversion) {

    /**
     * produce a new server
     * @param line one line in the file
     * @return constructed server
     */
    public static Server producer(String line) {
        String[] parts = line.split(",");

        boolean conversion;
        if (parts[4].equals("Yes")) {
            conversion = true;
        }

        else {
            conversion = false;
        }

        return new Server(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]), conversion);
    }
}
