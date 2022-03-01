package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

import ad.auction.dashboard.model.Utility;

//TODO replace ageGroup, income, context with enum values

public record Impression(LocalDateTime dateTime, String ID, Gender gender, String ageGroup, String income, String context,
        float impressionCost) {

    /**
     * produce a new impression
     * 
     * @param line one line in the file
     * @return constructed impression
     */
    public static Impression producer(String line) {
        String[] parts = line.split(",");

        return new Impression(
                Utility.parseDate(parts[0]),
                parts[1],
                parts[2].equals("Male") ? Gender.MALE : Gender.FEMALE,
                parts[3],
                parts[4],
                parts[5],
                Float.parseFloat(parts[6]));
    }

    public enum Gender {
        MALE,
        FEMALE;
    }


}
