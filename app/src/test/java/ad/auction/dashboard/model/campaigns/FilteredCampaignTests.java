package ad.auction.dashboard.model.campaigns;

import ad.auction.dashboard.model.Model;
import ad.auction.dashboard.model.files.records.Impression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

@Tag("model/campaigns")
public class FilteredCampaignTests {

    private static final Model model = new Model();

    /*HELPER*/

    @BeforeAll
    public static void setUp() {
        model.campaigns().newCampaign("2 week - test",
                "./data/2-week/click_log.csv",
                "./data/2-week/impression_log.csv",
                "./data/2-week/server_log.csv");
        model.campaigns().openCampaign("2 week - test");
    }

    @AfterAll
    public static void tearDown() {
        model.campaigns().removeCampaign("2 week - test");
        model.campaigns().closeAll();
    }

    @AfterEach
    public void clearFilters() {
        model.campaigns().getCurrentCampaign().toggleAllFilters(false);
    }

    /**/

    @Test
    @DisplayName("filter all")
    public void FilterAllTest() {
        int hash = model.campaigns().addUserFilter(r -> false);
        model.campaigns().toggleFilter(hash);

        assertEquals(0, model.campaigns().getCurrentCampaign().impressions().count());
    }

    /*GENDER*/

    @Test
    @DisplayName("Gender filter - male")
    public void genderFilterTestMale() {
        int hash = model.campaigns().addUserFilter(u -> u.gender() != Impression.Gender.Male);
        model.campaigns().toggleFilter(hash);

        long res = model.campaigns().getCurrentCampaign().impressions().count();
        assertEquals(324635,res);
    }

    @Test
    @DisplayName("Gender filter - female")
    public void genderFilterTestFemale() {
        int hash = model.campaigns().addUserFilter(u -> u.gender() != Impression.Gender.Female);
        model.campaigns().toggleFilter(hash);

        long res = model.campaigns().getCurrentCampaign().impressions().count();
        assertEquals(161469,res);
    }

    /*INCOME*/

    @Test
    @DisplayName("income filter - low")
    public void incomeFilterTestLow() {
        int hash = model.campaigns().addUserFilter(u -> u.income() != Impression.Income.Low);
        model.campaigns().toggleFilter(hash);

        long res = model.campaigns().getCurrentCampaign().impressions().count();
        assertEquals(340156, res);
    }

    @Test
    @DisplayName("income filter - medium")
    public void incomeFilterTestMedium() {
        int hash = model.campaigns().addUserFilter(u -> u.income() != Impression.Income.Medium);
        model.campaigns().toggleFilter(hash);

        long res = model.campaigns().getCurrentCampaign().impressions().count();
        assertEquals(243054, res);
    }

    @Test
    @DisplayName("income filter - high+low")
    public void incomeFilterTestHigh() {
        int hash = model.campaigns().addUserFilter(u -> u.income() != Impression.Income.Low && u.income() != Impression.Income.High);
        model.campaigns().toggleFilter(hash);

        long res = model.campaigns().getCurrentCampaign().impressions().count();
        assertEquals(243050, res);
    }

    /*Date filters*/

    @Test
    @DisplayName("Set start date after end date")
    public void invalidStartDateTest() {
        assertThrows(IllegalArgumentException.class,
                () -> model.campaigns().setDate(true, LocalDateTime.MAX));
    }

    @Test
    @DisplayName("Set end date before start date")
    public void invalidEndDateTest() {
        assertThrows(IllegalArgumentException.class,
                () -> model.campaigns().setDate(false, LocalDateTime.MIN));
    }
}
