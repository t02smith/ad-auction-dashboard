package ad.auction.dashboard.model.files.records;

public record User(Impression.Gender gender, Impression.AgeGroup ageGroup, Impression.Income income,
                   Impression.Context context) {

}
