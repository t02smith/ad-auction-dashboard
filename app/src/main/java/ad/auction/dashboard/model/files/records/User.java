package ad.auction.dashboard.model.files.records;

/**
 * Represents a user from the data files
 * @param gender the user's gender (Male/Female)
 * @param ageGroup the user's age group (<25/25-34/35-44/45-54/>54)
 * @param income the user's income (Low/Medium/High)
 * @param context the context of the impression (News/Shopping/Social Media/Blog/Hobbies/Travel)
 */
public record User(Impression.Gender gender, Impression.AgeGroup ageGroup, Impression.Income income,
                   Impression.Context context) {

}
