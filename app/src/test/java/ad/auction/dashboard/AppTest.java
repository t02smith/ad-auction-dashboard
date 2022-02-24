package ad.auction.dashboard;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    @Tag("setup")
    public void javaVersionTest() {
        int version = Integer.parseInt(System.getProperty("java.version"));
        assertTrue(version >= 17);

    }


}