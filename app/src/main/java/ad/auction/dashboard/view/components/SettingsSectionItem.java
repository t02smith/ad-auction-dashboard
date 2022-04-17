package ad.auction.dashboard.view.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SettingsSectionItem extends HBox {

    public SettingsSectionItem(String name, Node child) {
        this(name, null, child);
    }

    public SettingsSectionItem(String name, String note, Node child) {
        var nameLbl = new Label(name);
        nameLbl.getStyleClass().add("settings-section-item-title");
        var text = new VBox(nameLbl);

        if (note != null) {
            var noteLbl = new Label(note);
            noteLbl.getStyleClass().add("settings-note");
            text.getChildren().add(noteLbl);
        }

        var empty = new Region();
        HBox.setHgrow(empty, Priority.ALWAYS);

        this.getChildren().addAll(text, empty, child);
    }
}
