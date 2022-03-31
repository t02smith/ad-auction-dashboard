package ad.auction.dashboard.view.components;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Sidebar component for the user to select a metric
 * 
 * @author tcs1g20
 */
public class ButtonList<T> extends ScrollPane {
    
    private final Consumer<T> onClick;
    private final List<T> values;
    private final T defaultVal;
    private final boolean persist;

    private Label active;
    private HashMap<T, Boolean> isActive;


    /**
     * A list of buttons
     * @param values The buttons to be displayed
     * @param defaultVal The default button value
     * @param persist Toggle button/one button at a time
     * @param onClick When a button is clicked
     */
    public ButtonList(List<T> values, T defaultVal, boolean persist, Consumer<T> onClick) {
        this.onClick = onClick;
        this.persist = persist;
        this.values = values;
        this.defaultVal = defaultVal;

        if (persist) {
            this.isActive = new HashMap<>();
            values.forEach(v -> isActive.put(v, false));
            isActive.put(defaultVal, true);
        }


        this.build();
        this.setFocusTraversable(false);
    }

    private void build() {
        var elems = new VBox();
        this.getStyleClass().addAll("metric-panel","bg-primary");

        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setContent(elems);
        values.forEach((v -> {
            var btn = new Label(v.toString());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setMinWidth(300);
            btn.setOnMouseClicked(e -> {
                onClick.accept(v);

                if (!persist) {
                    if (this.active != null) {
                        this.active.getStyleClass().remove("btn-active");
                    }

                    this.active = btn;
                    this.active.getStyleClass().add("btn-active");
                } else {
                    if (this.isActive.get(v)) {
                        btn.getStyleClass().remove("btn-active");
                    } else {
                        btn.getStyleClass().add("btn-active");
                    }

                    this.isActive.put(v, !this.isActive.get(v));
                }
            });

            if (defaultVal != null) {
                if (v.equals(defaultVal)) {
                    this.active = btn;
                    this.active.getStyleClass().add("btn-active");
                }
            }

        	btn.getStyleClass().add("metric-btn");
            elems.getChildren().add(btn);
        }));
    }

}
