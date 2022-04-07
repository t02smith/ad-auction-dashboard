package ad.auction.dashboard.view.settings;

import javafx.scene.layout.BorderPane;

public abstract class SettingPage extends BorderPane {
    private String displayName;

    public SettingPage(String displayName) {
        this.displayName = displayName;

    }

    public abstract void updateView();

    public String getDisplayName() {
        return displayName;
    }

}
