package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.view.components.ButtonList;
import ad.auction.dashboard.view.components.TabMenu;
import ad.auction.dashboard.view.ui.Window;
import javafx.scene.layout.BorderPane;

/**
 *  Displays help settings for users to learn
 *  about the application
 */
public class InfoPage extends BasePage {

    private final BorderPane bp = new BorderPane();

    public InfoPage(Window window) {
        super(window);
    }

    @Override
    public void build() {
        this.root.getChildren().add(bp);

        var tabs = new TabMenu("Help");

        //TODO guides
        //TODO definitions
        //TODO
    }

}
