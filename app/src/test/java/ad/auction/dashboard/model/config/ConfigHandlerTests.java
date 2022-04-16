package ad.auction.dashboard.model.config;

import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.view.settings.Themes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("model")
public class ConfigHandlerTests {

    private ConfigHandler handler;

    @BeforeEach
    public void setUp() {
        this.handler = new ConfigHandler();
    }

    @Test
    @DisplayName("read test file")
    public void readTestFile() throws Exception {
        this.handler.parse(
                this.getClass().getResource("/data/config_test.xml").toExternalForm()
        );

        var actual = this.handler.getConfig();

        assertEquals(Metrics.TOTAL_COST, actual.defaultMetric());
        assertEquals(Themes.DARK, actual.theme());

        var c = actual.campaigns().get(0);
        assertEquals("Test campaign", c.name());
        assertEquals("fake/imp/path.csv",c.impPath());
        assertEquals("fake/clk/path.csv",c.clkPath());
        assertEquals("fake/svr/path.csv",c.svrPath());
    }


    @Test
    @DisplayName("Incorrect file location")
    public void incorrectFileLocation() {
        assertThrows(IOException.class, () -> this.handler.parse("fake.xml"));
    }
}
