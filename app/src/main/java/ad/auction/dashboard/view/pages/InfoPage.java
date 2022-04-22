package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.config.Guide;
import ad.auction.dashboard.model.config.GuideHandler;
import ad.auction.dashboard.view.components.BackBtn;
import ad.auction.dashboard.view.components.ButtonList;
import ad.auction.dashboard.view.components.GuideView;
import ad.auction.dashboard.view.components.TabMenu;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.util.Arrays;


/**
 *  Displays help settings for users to learn
 *  about the application
 */
public class InfoPage extends BasePage {

    private final BorderPane bp = new BorderPane();
    private GuideView guide;

    public InfoPage(Window window) {
        super(window);
    }

    @Override
    public void build() {
        this.root.getChildren().add(bp);

        this.bp.setTop(title());
        this.bp.getStyleClass().add("bg-primary");

        var tabs = new TabMenu("guides");
        this.bp.setLeft(tabs);

        var guides = guides();
        tabs.addPane("guides", guides);

        tabs.build();
        //TODO definitions
        //TODO
    }

    private ButtonList<Guide> guides() {

        //Read guides from file
        var gHandler = new GuideHandler();
        gHandler.parse();
        var guides = gHandler.getGuides();
        guides.add(metricList());

        ButtonList<Guide> bl;
        this.guide = new GuideView(guides.get(0), ref -> {
            var newG = guides.stream().filter(g -> g.id() == ref);
            var guide = newG.findFirst();
            guide.ifPresent(value -> this.guide.setGuide(value));
        });

        this.bp.setCenter(this.guide);
        bl = new ButtonList<>(guides, guides.get(0), false, g ->
                this.guide.setGuide(g));

        return bl;
    }

    private BorderPane title() {
        //Title background on top
        var title = new BorderPane();
        title.getStyleClass().add("topBackground");

        //Title text on top
        var mainMenuText = new Text("Info");
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);

        //Add back button
        var backButton = new BackBtn((e) -> window.startMenu());
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(0, 0 ,0, 3));
        title.setLeft(backButton);
        return title;
    }

    private Guide metricList() {
        return new Guide(4,"Metrics", "Understanding how to review your campaign", null, Arrays.stream(Metrics.values()).map(m ->
            new Guide.Section(m.getMetric().displayName(), m.getMetric().desc())
        ).toList());
    }

}
