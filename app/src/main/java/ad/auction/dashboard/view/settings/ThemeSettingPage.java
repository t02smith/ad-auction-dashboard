package ad.auction.dashboard.view.settings;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ThemeSettingPage extends SettingPage {

    public static ThemeEnum theme = ThemeEnum.DARK_THEME;

    public ThemeSettingPage() {
        super("Themes");
        // create label
        Label label = new Label("Choose a theme: ");
        label.getStyleClass().add("settingLabel");
        // create combobox
        ComboBox<String> themeCombo = new ComboBox<>();
        themeCombo.getStyleClass().add("comboBoxStyle");
        // add items
        themeCombo.setItems(FXCollections.observableArrayList("Dark Theme", "Light Theme"));
        themeCombo.setMinWidth(400);
        // add choose button
        Button button = new Button("Choose");
        button.getStyleClass().add("buttonStyle");

        HBox hbox = new HBox(label, themeCombo, button);
        hbox.setPadding(new Insets(40, 0, 0, 0));
        hbox.setAlignment(Pos.BASELINE_CENTER);
        this.setCenter(hbox);
        ThemeSettingPage page =this;

        button.setOnAction(event -> {
            // get selected index
            int idx = themeCombo.getSelectionModel().getSelectedIndex();
            if (idx > -1) {
                if (idx == 0) {
                    // update theme
                    theme = ThemeEnum.DARK_THEME;
                    page.getScene().getStylesheets().add(getClass().getResource(ThemeSettingPage.theme.getCssResource()).toExternalForm());
                } else if (idx == 1) {
                    // update theme
                    theme = ThemeEnum.LIGH_THEME;
                    page.getScene().getStylesheets().add(getClass().getResource(ThemeSettingPage.theme.getCssResource()).toExternalForm());
                }
            }
        });

    }

    @Override
    public void updateView() {

    }
}
