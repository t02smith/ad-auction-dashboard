package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

import ad.auction.dashboard.model.Utility;

public record Impression(LocalDateTime dateTime, long ID, Gender gender, AgeGroup ageGroup, Income income,
        Context context,
        float impressionCost) implements SharedFields {

    public static Impression producer(String[] line) {
        return new Impression(
                Utility.parseDate(line[0]), // Date of impression
                Long.parseLong(line[1]), // ID
                Gender.valueOf(line[2]), // Gender
                AgeGroup.getAgeGroup(line[3]), // Age Group
                Income.valueOf(line[4]), // Income
                Context.valueOf(line[5].replace(" ", "_")), // Context
                Float.parseFloat(line[6])); // Impression cost
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
