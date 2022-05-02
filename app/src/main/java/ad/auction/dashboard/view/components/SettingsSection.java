package ad.auction.dashboard.view.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * A group of related settings for the SettingsPage
 *
 * @author tcs1g20
 */
public class SettingsSection extends VBox {

    /**
     * Create a new SettingsSection
     * @param title the title of the section
     * @param children the section's content
     */
    public SettingsSection(String title, Node... children) {
        this.getStyleClass().add("settings-section");

        var titleLbl = new Label(title);
        titleLbl.getStyleClass().add("settings-section-title");
        titleLbl.setAlignment(Pos.TOP_CENTER);
        titleLbl.setPrefWidth(Double.MAX_VALUE);

        this.getChildren().add(titleLbl);
        this.getChildren().addAll(children);
        this.setSpacing(10);

    }

    /**
     * Adds a new node to the settings section
     * @param node node to add
     */
    public void addChild(Node node) {
        this.getChildren().add(node);
    }

}
