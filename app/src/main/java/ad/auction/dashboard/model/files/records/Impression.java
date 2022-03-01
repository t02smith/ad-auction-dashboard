package ad.auction.dashboard.model.files.records;

public record Impression(String dateTime, String ID, boolean isMale, String ageGroup, String income, String context,
                         float impressionCost) {

    /**
     * produce a new impression
     * @param line one line in the file
     * @return constructed impression
     */
    public static Impression producer(String line) {
        String[] parts = line.split(",");

        boolean isMale;
        if (parts[2].equals("Male")) {
            isMale = true;
        }

        else {
            isMale = false;
        }

        return new Impression(parts[0], parts[1], isMale, parts[3], parts[4], parts[5], Float.parseFloat(parts[6]));
    } 

}
