package ad.auction.dashboard.view.components;

import java.util.Arrays;
import java.util.function.Consumer;

import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.view.pages.CampaignPage;
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
    private Label active;

    public MetricSelection(Consumer<Metrics> loadMetric) {
        this.loadMetric = loadMetric;
        this.build();
        
        this.setFocusTraversable(false);
    }

    private void build() {
        var elems = new VBox();
        this.getStyleClass().addAll("metric-panel","bg-primary");

        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setContent(elems);
        Arrays.asList(Metrics.values()).forEach(m -> {
            var btn = new Label(m.getMetric().displayName());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnMouseClicked(e -> {
                loadMetric.accept(m);
                if (this.active != null)
                        this.active.getStyleClass().remove("btn-active");
                this.active = btn;
                this.active.getStyleClass().add("btn-active");
            });

            if (m == CampaignPage.DEFAULT_METRIC) {
                this.active = btn;
                this.active.getStyleClass().add("btn-active");
            }

        	btn.getStyleClass().add("metric-btn");
            elems.getChildren().add(btn);
        });
    }

}
