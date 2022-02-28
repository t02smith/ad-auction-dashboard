package ad.auction.dashboard.records;

import java.time.LocalDateTime;

public class Impression extends FileType {

    private boolean isMale; // true if male, false if female
    private String ageGroup;
    private String income;
    private String context;
    private float impressionCost;

    public Impression(LocalDateTime dateTime, String ID, boolean isMale, String ageGroup, String income, String context,
                      float impressionCost) {
        setDateTime(dateTime);
        setID(ID);
        setImpressionCost(impressionCost);
        setContext(context);
        setIncome(income);
        setAgeGroup(ageGroup);
        setMale(isMale);
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean isMale) {
        this.isMale = isMale;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public float getImpressionCost() {
        return impressionCost;
    }

    public void setImpressionCost(float impressionCost) {
        this.impressionCost = impressionCost;
    }
}
