package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

import ad.auction.dashboard.model.Utility;

public record Impression(LocalDateTime dateTime, long ID, Gender gender, AgeGroup ageGroup, Income income, Context context,
        float impressionCost) implements SharedFields {

    /**
     * produce a new impression
     * 
     * @param line one line in the file
     * @return constructed impression
     */
    public static Impression producer(String line) {
        String[] parts = line.split(",");



        return new Impression(
                Utility.parseDate(parts[0]),                //Date of impression
                Long.parseLong(parts[1]),                   //ID
                Gender.valueOf(parts[2].toUpperCase()),     //Gender
                AgeGroup.getAgeGroup(parts[3]),             //Age Group
                Income.valueOf(parts[4].toUpperCase()),     //Income
                Context.valueOf(parts[5].toUpperCase().replace(" ", "_")),    //Context
                Float.parseFloat(parts[6]));                //Impression cost
    }

    //ENUM

    public enum Gender {
        MALE,
        FEMALE;
    }

    public enum Income {
        LOW,
        MEDIUM,
        HIGH;
    }

    public enum AgeGroup {
        UNDER_25("<25"),
        BETWEEN_25_34("25-34"),
        BETWEEN_35_44("35-44"),
        BETWEEN_45_54("45-54"),
        OVER_54(">54");

        private final String str;
        
        private AgeGroup(String str) {this.str=str;}

        public static AgeGroup getAgeGroup(String arg) {
            for (AgeGroup ag: AgeGroup.values()) {
                if (ag.str.equals(arg)) return ag;
            }
            return null;
        }
    }

    public enum Context {
        NEWS,
        SHOPPING,
        SOCIAL_MEDIA,
        BLOG,
        HOBBIES,
        TRAVEL;
    }


}
