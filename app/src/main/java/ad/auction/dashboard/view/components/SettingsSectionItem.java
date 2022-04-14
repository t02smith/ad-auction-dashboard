package ad.auction.dashboard.view.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class SettingsSectionItem extends HBox {

    public SettingsSectionItem(String name, Node child) {
        var nameLbl = new Label(name);
        nameLbl.getStyleClass().add("settings-section-item-title");

        var empty = new Region();
        HBox.setHgrow(empty, Priority.ALWAYS);

        this.getChildren().addAll(nameLbl, empty, child);
    }
}
