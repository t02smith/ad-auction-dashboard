package ad.auction.dashboard.model.files;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import ad.auction.dashboard.model.Util;
import ad.auction.dashboard.model.files.records.Impression;
import ad.auction.dashboard.model.files.records.Impression.*;
import ad.auction.dashboard.model.files.records.Server;
import ad.auction.dashboard.model.files.records.Click;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Tag("model/files")
public class RecordTest {

    @Test
    @DisplayName("Impression producer")
    public void impressionProducerTest() {
        final String line = "2015-01-01 12:00:02,4620864431353617408,Male,25-34,High,Blog,0.001713";
        final Impression actual = (Impression)FileType.IMPRESSION.produce(line.split(","));

        assertTrue(
            actual.dateTime().equals(Util.parseDate("2015-01-01 12:00:02")) &&
            actual.ID() == 4620864431353617408L &&
            actual.gender() == Gender.Male &&
            actual.ageGroup() == AgeGroup.BETWEEN_25_34 &&
            actual.income() == Income.High &&
            actual.context() == Context.Blog &&
            actual.impressionCost() == (float)0.001713
        );
        

    }

    @Test
    @DisplayName("Server producer")
    public void serverProducerTest() {
        final String line = "2015-01-01 12:04:13,8370837523317244928,2015-01-01 12:09:50,10,No";
        final Server actual = (Server)FileType.SERVER.produce(line.split(","));

        assertTrue(
            actual.dateTime().equals(Util.parseDate("2015-01-01 12:04:13")) &&
            actual.ID() == 8370837523317244928L &&
            actual.exitDate().equals(Util.parseDate("2015-01-01 12:09:50")) &&
            actual.pagesViewed() == 10 &&
            !actual.conversion()
        );
    }

    @Test
    @DisplayName("Click producer")
    public void clickProducerTest() {
        final String line = "2015-01-01 12:59:29,541711580562437120,8.762051";
        final Click actual = (Click)FileType.CLICK.produce(line.split(","));

        assertTrue(
            actual.dateTime().equals(Util.parseDate("2015-01-01 12:59:29")) &&
            actual.ID() == 541711580562437120L &&
            actual.clickCost() == 8.762051f
        );
    }

    @ParameterizedTest(name="{index} -  invalid imp record test")
    @ValueSource(strings = {
            "2015-01-01 12:00:02,4620864431353617408,Male,25-34,High,Blog,-12",
            "2015-01-01 12:00:02,4620864431353617408,Fake Gender,25-34,High,Blog,0.002265",
            "2015-01-01 12:00:02,-4,Female,25-34,High,Blog,0.002265",
            "2015-01-01 12:00:02,4620864431353617408,Male,25-34,massive,Blog,0.002265",
            "2015-01-01 12:00:02,4620864431353617408,Male,25-34,High,elsewhere,0.002265",
            "2015-01-01,4620864431353617408,Female,25-34,High,Blog,0.002265"
    })
    public void invalidImpressionRecordTest(String record) {
        assertThrows(IllegalArgumentException.class,
                () -> FileType.IMPRESSION.produce(record.split(",")));
    }

    @ParameterizedTest(name="{index} - invalid clk record test")
    @ValueSource(strings = {
            "12:01:21,8895519749317550080,11.794442",
            "2015-01-01 12:01:21,-20,11.794442",
            "2015-01-01 12:01:21,8895519749317550080,-90"
    })
    public void invalidClickRecordTest(String record) {
        assertThrows(IllegalArgumentException.class,
                () -> FileType.CLICK.produce(record.split(",")));
    }

    @ParameterizedTest(name="{index} - invalid svr record test")
    @ValueSource(strings = {
            "2015-01 12:21,8895519749317550080,2015-01-01 12:05:13,7,No",
            "2015-01-01 12:01:21,-12,2015-01-01 12:05:13,7,No",
            "2015-01-01 12:01:21,8895519749317550080,never,7,No",
            "2015-01-01 12:01:21,8895519749317550080,n/a,-3,No",
            "2015-01-01 12:01:21,8895519749317550080,2015-01-01 12:05:13,7,fail"
    })
    public void invalidServerRecordTest(String record) {
        assertThrows(IllegalArgumentException.class,
                () -> FileType.SERVER.produce(record.split(",")));
    }

}
