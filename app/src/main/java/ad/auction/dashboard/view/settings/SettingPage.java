package ad.auction.dashboard.view.settings;

import ad.auction.dashboard.view.components.SettingsSection;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public abstract class SettingPage extends BorderPane {
    private final String displayName;
    private final VBox sections = new VBox();

    public SettingPage(String displayName) {
        this.displayName = displayName;
        this.setCenter(sections);
    }

    protected void addSection(SettingsSection section) {
        this.sections.getChildren().add(section);
    }

    public abstract void updateView();

    public String getDisplayName() {
        return displayName;
    }

}
