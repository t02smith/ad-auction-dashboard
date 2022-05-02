package ad.auction.dashboard.view.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.HashMap;

/**
 * A sidebar component that can have many tabs with different content
 *
 * @author tcs1g20
 */
public class TabMenu extends StackPane  {

    //Each tab <name, content>
    private final HashMap<String, Node> panes = new HashMap<>();
    private final String defaultNode;
    private Label active;

    /**
     * Create a new TabMenu
     * @param defaultNode the name of the default tab
     */
    public TabMenu(String defaultNode) {
        this.defaultNode = defaultNode;
    }

    /**
     * Add a new tab into the TabMenu
     * @param name the tab's name
     * @param pane th tab's content
     */
    public void addPane(String name, Node pane) {
        this.panes.put(name, pane);
    }

    /**
     * Builds the TabMenu
     * Should be used after all tabs have been added
     */
    public void build() {
        HBox options = new HBox();
        this.getStyleClass().add("bg-primary");

        Region empty = new Region();
        empty.getStyleClass().add("bg-secondary");
        VBox.setVgrow(empty, Priority.ALWAYS);
        VBox sidebar = new VBox();

        panes.keySet().forEach(p -> {
            var tab = new Label(p);
            tab.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(tab, Priority.ALWAYS);
            tab.getStyleClass().addAll("tab-btn","bg-secondary");
            tab.setOnMouseClicked(e -> {
                sidebar.getChildren().clear();
                sidebar.getChildren().addAll(panes.get(p), empty, options);
                if (this.active != null)
                    this.active.getStyleClass().remove("btn-active");
                this.active = tab;
                this.active.getStyleClass().add("btn-active");
            });
            if (p.equals(defaultNode)) {
                this.active = tab;
                this.active.getStyleClass().add("btn-active");
            }
            options.getChildren().add(tab);
        });


        sidebar.getChildren().addAll(panes.get(defaultNode), empty, options);

        this.getChildren().add(sidebar);
    }

}
