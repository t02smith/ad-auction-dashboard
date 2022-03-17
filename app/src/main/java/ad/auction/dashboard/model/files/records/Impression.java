package ad.auction.dashboard.model.files.records;

import java.time.LocalDateTime;

import ad.auction.dashboard.model.Utility;

public record Impression(LocalDateTime dateTime, long ID, Gender gender, AgeGroup ageGroup, Income income, Context context,
        float impressionCost) implements SharedFields {

    
    public static Impression producer(String[] line) {
        return new Impression(
                Utility.parseDate(line[0]),                //Date of impression
                Long.parseLong(line[1]),                   //ID
                Gender.valueOf(line[2]),     //Gender
                AgeGroup.getAgeGroup(line[3]),             //Age Group
                Income.valueOf(line[4]),     //Income
                Context.valueOf(line[5].replace(" ", "_")),    //Context
                Float.parseFloat(line[6]));                //Impression cost
    }

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
        Male,
        Female;
    }

    public enum Income {
        Low,
        Medium,
        High;
    }

    public enum AgeGroup {
        UNDER_25("<25"),
        BETWEEN_25_34("25-34"),
        BETWEEN_35_44("35-44"),
        BETWEEN_45_54("45-54"),
        OVER_54(">54");

        public final String str;
        
        private AgeGroup(String str) {this.str=str;}

        public static AgeGroup getAgeGroup(String arg) {
            for (AgeGroup ag: AgeGroup.values()) {
                if (ag.str.equals(arg)) return ag;
            }
            return null;
        }
    }

    public enum Context {
        News("News"),
        Shopping("Shopping"),
        Social_Media("Social Media"),
        Blog("Blog"),
        Hobbies("Hobbies"),
        Travel("Travel");

        private String display;

        private Context(String display) {this.display = display;}

        public static Context getFromString(String s) {
            for (Context c: Context.values()) {
                if (c.display.equals(s)) return c;
            }
            return null;
        }
    }


}
