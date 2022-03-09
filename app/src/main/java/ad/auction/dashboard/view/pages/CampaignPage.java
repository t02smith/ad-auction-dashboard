package ad.auction.dashboard.view.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.view.ui.SideMenu;
import ad.auction.dashboard.view.ui.Window;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Advertisement page that holds the UI part of the graphs and controls
 *
 * @author hhg1u20
>>>>>>> origin/ui-with-upload-function
 */
public class CampaignPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CampaignPage.class.getSimpleName());

    private final String campaignName;

    private Rectangle graph;

    /**
     * Handles the new page according to the graph to be plotted
     *
     * @param window     - the window opened
     * @param graphIndex - the graph to be inserted
     *                   <p>
     */
    public CampaignPage(Window window, String campaignName) {
        super(window);
        this.campaignName = campaignName;
        this.graph = new Rectangle();

    }

    /**
     * Builds the UI elements on the page
     */
    @Override
    public void build() {
        logger.info("Building Advert Page with graph: FromFile");

        //The root is a stack pane
        root = new StackPane();
        root.setMaxWidth(window.getWidth());
        root.setMaxHeight(window.getHeight());

        //Root holds Border Pane, Border Pane holds everything else
        var menuPane = new BorderPane();
        root.getChildren().add(menuPane);

        //Title background on top
        var title = new BorderPane();
        title.getStyleClass().add("topBackground");

        //BorderPane to hold the graph and the buttons under it
        var graphPane = new BorderPane();

        //HBox to hold the buttons below the graph
        var graphButtonPane = new HBox();

        //Buttons below the graph
        var filterButton = new Button("Filter Options");
        var timeButton = new Button("Change Timespan");

        //Back button
        var backButton = new Button("<");
        backButton.getStyleClass().add("buttonStyle");
        backButton.setOnMouseClicked((e) -> window.startMenu());

        //Title text on top
        var mainMenuText = new Text("Advertisement - FromFile");
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);

        graphButtonPane.getChildren().addAll(filterButton, timeButton);
        graphPane.setBottom(graphButtonPane);
        //TODO: graphPane.setCenter(graph);

        menuPane.setTop(title);
        menuPane.setLeft(new SideMenu(window));
        menuPane.setRight(backButton);
        menuPane.setCenter(graphPane);

        //Style the buttons under the graph
        styleButtons(graphButtonPane);
    }

    /**
     * Update the graph according to which button was pressed in the side menu
     *
     * @param category - the category by which the graph needs to be recalculated
     */
    @Override
    public void update(int category) {
        switch (category) {
            case 0:
                //calc graph for num of clicks
                logger.info("Plotting graph for Number of clicks");
                break;
            case 1:
                //calc graph for num of uniques
                logger.info("Plotting graph for Number of uniques");
                break;
            case 2:
                //calc graph for num of convers
                logger.info("Plotting graph for Number of conversions");
                break;
            case 3:
                //calc graph for money spent
                logger.info("Plotting graph for Money spent");
                break;
            case 4:
                //calc graph for ctr
                logger.info("Plotting graph for CTR");
                break;
            case 5:
                //calc graph for cpa
                logger.info("Plotting graph for CPA");
                break;
            case 6:
                //calc graph for cpm
                logger.info("Plotting graph for CPM");
                break;
            case 7:
                //calc graph for bounce rate
                logger.info("Plotting graph for Bounce rate");
                break;
            case 8:
                //calc graph for click cost
                logger.info("Plotting graph for Click cost");
                break;
            case 9:
                //print
                logger.info("Print");
                break;
        }
    }

    /**
     * Style the buttons in a pane
     *
     * @param pane - the pane that holds only buttons
     */
    private void styleButtons(Pane pane) {
        var iter = pane.getChildren().iterator();

        while (iter.hasNext()) {
            iter.next().getStyleClass().add("buttonStyle");
        }
    }
}
