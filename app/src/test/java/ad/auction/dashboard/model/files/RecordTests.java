package ad.auction.dashboard.model.files;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Impression.Gender;

public class RecordTests {
    
    //TODO add unit tests for record producers

    @Test
    @DisplayName("Impression producer")
    @Tag("model/files")
    public void impressionProducer() {
        String line = "2015-01-01 12:00:02,4620864431353617408,Male,25-34,High,Blog,0.001713";

        Impression actual = (Impression)FileType.IMPRESSION.produce(line);

        System.out.println(actual.toString());

        assertTrue(
            actual.dateTime().equals(Utility.parseDate("2015-01-01 12:00:02")) &&
            actual.ID().equals("4620864431353617408") &&
            actual.gender() == Gender.MALE &&
            actual.ageGroup().equals("25-34") &&
            actual.income().equals("High") &&
            actual.context().equals("Blog") &&
            actual.impressionCost() == (float)0.001713
        );
        

    }
}
