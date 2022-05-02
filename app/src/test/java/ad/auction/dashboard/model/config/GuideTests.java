package ad.auction.dashboard.model.config;

import ad.auction.dashboard.TestUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GuideTests {

    private static final String GUIDE_LOCATION = TestUtility.getResourceFile("/data/guides/guide_test.xml");

    private static final GuideHandler gh = new GuideHandler();

    @BeforeAll
    public static void setUp() {
        gh.parse(GUIDE_LOCATION);
    }

    @Test
    @DisplayName("Parse guides test")
    public void parseGuidesTest() {
        var g = gh.getGuides();
        assertEquals(3, g.size(), "Not all guides read");
    }

    @Test
    @DisplayName("Parse guides - just steps")
    public void justStepsTest() {
        var g = gh.getGuides().get(0);

        assertNull(g.sections());
        assertEquals(8, g.steps().size());
        assertTrue(g.steps().get(1).isLink());
        assertEquals("Upload a campaign", g.name());
        assertEquals("How to upload your campaigns to Ad Auction Dashboard", g.desc());
        assertEquals(1, g.id());
        assertEquals("Hit \"Submit\"", g.steps().get(5).text());
    }

    @Test
    @DisplayName("Parse guides - just sections")
    public void justSectionsTest() {
        var g = gh.getGuides().get(1);

        assertNull(g.steps());
        assertEquals(3, g.sections().size());
        assertEquals("Log files", g.name());
        assertEquals("Understanding your campaign's data", g.desc());
        assertEquals("Server", g.sections().get(2).name());
        assertEquals("This log file is for all instances in which an ad was clicked on by a user. For each click we know the time, what user clicked on it, and how much that click costed.", g.sections().get(0).text());
    }

    @Test
    @DisplayName("Parse guides - sections & steps")
    public void sectionsAndStepsTest() {
        var g = gh.getGuides().get(2);

        assertEquals(5, g.steps().size());
        assertEquals(3, g.sections().size());

    }
}
