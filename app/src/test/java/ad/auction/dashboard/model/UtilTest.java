package ad.auction.dashboard.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class UtilTest {
    

    @Test
    @DisplayName("Utility parse date test")
    @Tag("model")
    public void parseDateTest() {
        final String date = "2015-01-01 12:01:47";
        final LocalDateTime actual = Util.parseDate(date);

        assertTrue(
            actual.getDayOfMonth() == 1 &&
            actual.getMonthValue() == 1 &&
            actual.getYear() == 2015 &&
            actual.getHour() == 12 &&
            actual.getMinute() == 1 &&
            actual.getSecond() == 47
        );
    }
}
