package ad.auction.dashboard.view.components;

import ad.auction.dashboard.model.config.Guide;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class GuideView extends VBox {

    private final Guide guide;

    public GuideView(Guide g) {
        this.guide = g;
        this.build();
    }

    private void build() {
        this.getStyleClass().addAll("guide", "bg-primary");
        this.getChildren().addAll(
                name(),
                description(),
                guide.steps() != null ? steps(): new Text(),
                guide.sections() != null ? sections(): new Text()
        );
    }

    private Label name() {
        var name = new Label(guide.name());
        name.setPrefWidth(Double.MAX_VALUE);
        name.setAlignment(Pos.CENTER);
        name.getStyleClass().add("guide-name");
        return name;
    }

    private Label description() {
        var desc = new Label(guide.desc());
        desc.setPrefWidth(Double.MAX_VALUE);
        desc.setAlignment(Pos.CENTER);
        desc.getStyleClass().add("guide-desc");
        return desc;
    }

    private VBox steps() {
        var steps = new VBox();
        steps.setPadding(new Insets(15));

        int[] step = new int[] {1};
        guide.steps().forEach(s -> {
            var lbl = new Label();
            if (s.isLink()) {
                lbl.setText(" -> " + s.text());
                lbl.getStyleClass().add("guide-link");
            } else {
                lbl.setText(step[0] + ": " + s.text());
                lbl.getStyleClass().add("guide-step");
                step[0]++;
            }
            steps.getChildren().add(lbl);
        });

        return steps;
    }

    private VBox sections() {
        var secs = new VBox();
        secs.setSpacing(15);
        secs.setPadding(new Insets(15));

        guide.sections().forEach(s -> {
            var title = new Label(s.name());
            title.setPrefWidth(Double.MAX_VALUE);
            title.getStyleClass().add("settings-section-title");

            var desc = new Label(s.text());
            desc.setWrapText(true);
            desc.getStyleClass().add("guide-step");

            secs.getChildren().add(new VBox(title,desc));
        });

        return secs;
    }
}
