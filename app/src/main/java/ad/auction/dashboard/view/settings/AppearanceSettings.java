package ad.auction.dashboard.view.settings;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.view.components.SettingsSection;
import ad.auction.dashboard.view.components.SettingsSectionItem;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class AppearanceSettings extends SettingPage {
    private final Controller controller = App.getInstance().controller();

    public AppearanceSettings() {
        super("Appearance");
        // create label
        Label label = new Label("Choose a theme: ");
        label.getStyleClass().add("settingLabel");

        //THEME SELECTION
        ComboBox<Themes> themeCombo = new ComboBox<>();
        themeCombo.setItems(FXCollections.observableArrayList(Themes.values()));
        themeCombo.setMinWidth(400);
        themeCombo.setValue(App.getInstance().controller().getTheme());
        themeCombo.setOnAction(e -> {
            this.getScene().getStylesheets().remove(getClass().getResource(controller.getTheme().getCssResource()).toExternalForm());
            this.getScene().getStylesheets().add(getClass().getResource(themeCombo.getValue().getCssResource()).toExternalForm());
            controller.setTheme(themeCombo.getValue());
        });

        this.setCenter(new SettingsSection("Themes", new SettingsSectionItem("Theme", themeCombo)));
    }

    @Override
    public void updateView() {

    }
}
