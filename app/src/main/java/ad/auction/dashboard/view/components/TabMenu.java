package ad.auction.dashboard.view.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.HashMap;

public class TabMenu extends StackPane  {

    private final HashMap<String, Node> panes = new HashMap<>();
    private final String defaultNode;
    private Label active;

    public TabMenu(String defaultNode) {
        this.defaultNode = defaultNode;
    }

    public void addPane(String name, Node pane) {
        this.panes.put(name, pane);
    }

    public void build() {
        HBox options = new HBox();
        this.getStyleClass().add("bg-primary");

        Region empty = new Region();
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
