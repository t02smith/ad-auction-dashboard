package ad.auction.dashboard.view.components;

import ad.auction.dashboard.App;
import ad.auction.dashboard.model.config.Guide;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * Display component for a guide
 *
 * @author tcs1g20
 */
public class GuideView extends ScrollPane {

    //Currently open guide
    private Guide guide;

    //What to do when a link is clicked in a guide
    private final Consumer<Integer> openLink;

    /**
     * Create a new guide view to display guides
     * @param g the default guide
     * @param openLink function to open a guide link
     */
    public GuideView(Guide g, Consumer<Integer> openLink) {
        this.guide = g;
        this.openLink = openLink;
        this.getStyleClass().add("scroll-pane");
        this.setHbarPolicy(ScrollBarPolicy.NEVER);
        this.setMaxWidth(App.getInstance().window().getWidth() - 300);
        App.getInstance().window().widthProperty().addListener(e -> this.setMaxWidth(App.getInstance().window().getWidth() - 300));

        this.build();
    }

    /**
     * Builds the GuideView
     */
    private void build() {
        var vb = new VBox(
                name(),
                description(),
                guide.steps() != null ? steps(): new Text(),
                guide.sections() != null ? sections(): new Text()
        );
        vb.setMaxWidth(App.getInstance().window().getWidth() - 300);
        App.getInstance().window().widthProperty().addListener(e -> vb.setMaxWidth(App.getInstance().window().getWidth() - 300));
        vb.getStyleClass().addAll("guide, bg-primary");
        this.setContent(vb);

    }

    /**
     * @return label for the guide's title
     */
    private Label name() {
        var name = new Label(guide.name());
        name.setPrefWidth(Double.MAX_VALUE);
        name.setAlignment(Pos.CENTER);
        name.getStyleClass().add("guide-name");
        return name;
    }

    /**
     * @return label for guide's description
     */
    private Label description() {
        var desc = new Label(guide.desc());
        desc.setPrefWidth(Double.MAX_VALUE);
        desc.setAlignment(Pos.CENTER);
        desc.getStyleClass().add("guide-desc");
        return desc;
    }

    /**
     * @return list of steps from the guide
     */
    private VBox steps() {
        var steps = new VBox();
        steps.setPadding(new Insets(15));

        int[] step = new int[] {1};
        guide.steps().forEach(s -> {
            var lbl = new Label();
            if (s.isLink()) {
                lbl.setText(" -> " + s.text());
                lbl.getStyleClass().add("guide-link");
                lbl.setOnMouseClicked(e -> this.openLink.accept(s.ref()));
            } else {
                lbl.setText(step[0] + ": " + s.text());
                lbl.getStyleClass().add("guide-step");
                step[0]++;
            }
            steps.getChildren().add(lbl);
        });

        return steps;
    }

    /**
     * @return list of guide sections
     */
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

    /**
     * Changes the active guide
     * @param g the new guide
     */
    public void setGuide(Guide g) {
        this.guide = g;
        this.build();
    }
}
