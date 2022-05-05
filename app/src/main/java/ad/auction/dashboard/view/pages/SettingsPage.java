package ad.auction.dashboard.view.pages;

import java.util.Arrays;

import ad.auction.dashboard.view.components.BackBtn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.view.settings.Settings;
import ad.auction.dashboard.view.components.ButtonList;
import ad.auction.dashboard.view.components.TabMenu;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Creates the settings page
 */
public class SettingsPage extends BasePage{

    private static final Logger logger = LogManager.getLogger(SettingsPage.class.getSimpleName());
    private final BorderPane screen = new BorderPane();
    public static final Settings DEFAULT_SETTINGS = Settings.THEMES;
    public BorderPane title;

    public SettingsPage(Window window) {
        super(window);
    }

    /**
     * Builds the UI element of the settings page
     */
    @Override
    public void build() {
        logger.info("Building Main Menu");
        //The root is a stack pane
        root = new StackPane();
        root.setMaxWidth(window.getWidth());
        root.setMaxHeight(window.getHeight());
        root.getChildren().add(screen);
        var settingPane = new BorderPane();
        settingPane.getStyleClass().add("bg-primary");
        var menu = new TabMenu("settings");
        // load default pane
        settingPane.setCenter(DEFAULT_SETTINGS.getPage());
        DEFAULT_SETTINGS.getPage().updateView();
        menu.addPane("settings", new ButtonList<>(Arrays.asList(Settings.values()), DEFAULT_SETTINGS, false, settings -> {
            // load selected setting pane
            settingPane.setCenter(settings.getPage());
            settings.getPage().updateView();
        }));
        menu.build();
        screen.setTop(title());
        screen.setLeft(menu);
        screen.setCenter(settingPane);
    }

    private BorderPane title() {
        //Title background on top
        var title = new BorderPane();
        title.getStyleClass().add("topBackground");

        //Title text on top
        var mainMenuText = new Text("Settings");
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);

        //Add back button
        var backButton = new BackBtn((e) -> window.startMenu());
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(0, 0 ,0, 3));
        title.setLeft(backButton);
        return title;
    }

}