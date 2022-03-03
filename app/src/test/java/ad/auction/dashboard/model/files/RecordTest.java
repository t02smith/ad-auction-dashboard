package ad.auction.dashboard.model.files;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.model.Utility;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Impression.*;
import ad.auction.dashboard.model.files.records.Server;
import ad.auction.dashboard.model.files.records.Click;

public class RecordTest {

    @Test
    @DisplayName("Impression producer")
    @Tag("model/files")
    public void impressionProducerTest() {
        final String line = "2015-01-01 12:00:02,4620864431353617408,Male,25-34,High,Blog,0.001713";
        final Impression actual = (Impression)FileType.IMPRESSION.produce(line);

        assertTrue(
            actual.dateTime().equals(Utility.parseDate("2015-01-01 12:00:02")) &&
            actual.ID() == 4620864431353617408L &&
            actual.gender() == Gender.MALE &&
            actual.ageGroup() == AgeGroup.BETWEEN_25_34 &&
            actual.income() == Income.HIGH &&
            actual.context() == Context.BLOG &&
            actual.impressionCost() == (float)0.001713
        );
        

    }

    @Test
    @DisplayName("Server producer")
    @Tag("model/files")
    public void serverProducerTest() {
        final String line = "2015-01-01 12:04:13,8370837523317244928,2015-01-01 12:09:50,10,No";
        final Server actual = (Server)FileType.SERVER.produce(line);

        assertTrue(
            actual.dateTime().equals(Utility.parseDate("2015-01-01 12:04:13")) &&
            actual.ID() == 8370837523317244928L &&
            actual.exitDate().equals(Utility.parseDate("2015-01-01 12:09:50")) &&
            actual.pagesViewed() == 10 &&
            !actual.conversion()
        );
    }

    @Test
    @DisplayName("Click producer")
    @Tag("model/files")
    public void clickProducerTest() {
        final String line = "2015-01-01 12:59:29,541711580562437120,8.762051";
        final Click actual = (Click)FileType.CLICK.produce(line);

        assertTrue(
            actual.dateTime().equals(Utility.parseDate("2015-01-01 12:59:29")) &&
            actual.ID() == 541711580562437120L &&
            actual.clickCost() == 8.762051f
        );
    }
}
