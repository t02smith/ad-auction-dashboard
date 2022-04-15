package ad.auction.dashboard.view.pages;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.view.settings.Settings;
import ad.auction.dashboard.view.components.ButtonList;
import ad.auction.dashboard.view.components.TabMenu;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class SettingsPage extends BasePage{
    private static final Logger logger = LogManager.getLogger(SettingsPage.class.getSimpleName());
    private final BorderPane screen = new BorderPane();
    public static final Settings DEFAULT_SETTINGS = Settings.THEMES;
    public BorderPane mainPane;
    public BorderPane title;
    public Text mainMenuText;
    public Label modeLabel;
    public SettingsPage(Window window) {
        super(window);
    }

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
        menu.addPane("settings", new ButtonList<Settings>(Arrays.asList(Settings.values()), DEFAULT_SETTINGS, false, settings -> {
            // load selected setting pane
            settingPane.setCenter(settings.getPage());
            settings.getPage().updateView();
        }));
        menu.build();
        screen.setTop(title());
        screen.setLeft(menu);
        screen.setCenter(settingPane);
    }


    private StackPane buildBackButton() {
        //Create the button
        var backButton = new Button();
        backButton.getStyleClass().add("buttonStyle");
        backButton.setMaxWidth(50);
        backButton.setMaxHeight(40);
        var buttonWidth = backButton.getMaxWidth();
        var buttonHeight = backButton.getMaxHeight();
        backButton.setOnMouseClicked((e) -> window.startMenu());


        // Draw arrow with canvas
        var canvas = new Canvas(buttonWidth, buttonHeight);
        canvas.setMouseTransparent(true);

        var gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.strokeLine(buttonWidth * 0.25, buttonHeight * 0.5, buttonWidth * 0.75,  buttonHeight * 0.5);
        gc.strokeLine(buttonWidth * 0.25, buttonHeight * 0.5, buttonWidth * 0.5, buttonHeight * 0.725);
        gc.strokeLine(buttonWidth * 0.25, buttonHeight * 0.5, buttonWidth * 0.5, buttonHeight * 0.275);

        // Stack the drawn arrow on the button
        var pane = new StackPane();
        pane.getChildren().addAll(backButton, canvas);

        return pane;
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
        var backButton = buildBackButton();
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(0, 0 ,0, 3));
        title.setLeft(backButton);
        return title;
    }

}