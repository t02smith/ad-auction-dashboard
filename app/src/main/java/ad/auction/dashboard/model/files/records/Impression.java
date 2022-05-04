package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import ad.auction.dashboard.model.Util;

/**
 * A data record from the impression log
 * @param dateTime the date & time of the impression
 * @param ID the user's uid
 * @param gender the user's gender (Male/Female)
 * @param ageGroup the user's age group (<25/25-34/35-44/45-54/>54)
 * @param income the user's income (Low/Medium/High)
 * @param context the context of the impression (News/Shopping/Social Media/Blog/Hobbies/Travel)
 * @param impressionCost the cost of this impression
 */
public record Impression(LocalDateTime dateTime, long ID, Gender gender, AgeGroup ageGroup, Income income,
        Context context, float impressionCost) implements SharedFields {

    /**
     * Create an Impression data record based off a single line from the impression log
     * @param line the line from the impression log
     * @return the generate Impression data record
     */
    public static Impression producer(String[] line) {

        try {
            float ic = Float.parseFloat(line[6]);
            if (ic < 0) throw new IllegalArgumentException("Impression cost must be above 0");

            long id = Long.parseLong(line[1]);
            if (id < 0) throw new IllegalArgumentException("ID must be a positive value");

            return new Impression(
                    Util.parseDate(line[0]), // Date of impression
                    id, // ID
                    Gender.valueOf(line[2]), // Gender
                    AgeGroup.getAgeGroup(line[3]), // Age Group
                    Income.valueOf(line[4]), // Income
                    Context.valueOf(line[5].replace(" ", "_")), // Context
                    ic); // Impression cost


        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid impression field");
        }
    }

    // ENUM

    public enum Gender {
        Male,
        Female
    }

    public enum Income {
        Low,
        Medium,
        High
    }

    public enum AgeGroup {
        UNDER_25("<25"),
        BETWEEN_25_34("25-34"),
        BETWEEN_35_44("35-44"),
        BETWEEN_45_54("45-54"),
        OVER_54(">54");

        public final String str;
        private final int hash;

        AgeGroup(String str) {
            this.str = str;
            this.hash = str.hashCode();
        }

        public static AgeGroup getAgeGroup(String arg) {
            int hash = arg.hashCode();
            for (AgeGroup ag : AgeGroup.values()) {
                if (ag.hash == hash)
                    return ag;
            }
            return null;
        }
    }

    public enum Context {
        News,
        Shopping,
        Social_Media,
        Blog,
        Hobbies,
        Travel
    }

}
