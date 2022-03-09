package ad.auction.dashboard.view.components;

import java.util.Arrays;
import java.util.function.Consumer;

import ad.auction.dashboard.model.calculator.Metrics;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Sidebar component for the user to select a metric
 * 
 * @author tcs1g20
 */
public class MetricSelection extends ScrollPane {
    
    private final Consumer<Metrics> loadMetric;

    public MetricSelection(Consumer<Metrics> loadMetric) {
        this.loadMetric = loadMetric;
        this.build();
    }

    private void build() {
        var elems = new VBox();
        elems.getStyleClass().add("metric-panel");

        this.setContent(elems);


        Arrays.asList(Metrics.values()).forEach(m -> {
            var btn = new Label(m.toString().replace("_", " ").toLowerCase());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnMouseClicked(e -> {
                loadMetric.accept(m);
            });

            VBox.setMargin(btn, new Insets(10, 10, 10, 10));
        	btn.getStyleClass().add("buttonStyle");

            elems.getChildren().add(btn);
        });
    }
}
