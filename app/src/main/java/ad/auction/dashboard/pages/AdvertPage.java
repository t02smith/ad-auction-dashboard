package ad.auction.dashboard.pages;

import ad.auction.dashboard.storage.CSVFolderDatabase;
import ad.auction.dashboard.storage.FileUtils;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.ui.SideMenu;
import ad.auction.dashboard.ui.Window;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import java.io.File;
import java.util.Objects;

/**
 * Advertisement page that holds the UI part of the graphs and controls
 *
 * @author hhg1u20
>>>>>>> origin/ui-with-upload-function
 */
public class AdvertPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(AdvertPage.class.getSimpleName());

    private Rectangle graph;
    // create a database when the advertisement page created
    private CSVFolderDatabase database = new CSVFolderDatabase();
    // the data folder path for the advertisement page
    private String folderPath;

    public AdvertPage(Window window) {
        super(window);
    }

    /**
     * Handles the new page according to the graph to be plotted
     *
     * @param window     - the window opened
     * @param graphIndex - the graph to be inserted
     *                   <p>
     *                   TODO: Change *int graph* to the actual graph, and initialise in the constructor
     */
    public AdvertPage(Window window, int graphIndex) {
        super(window);
        this.graph = new Rectangle();
        // range check
        if (graphIndex >= 0 && graphIndex < database.getFolders().size()) {
            // get folder path
            folderPath = database.getFolders().get(graphIndex);
            // get folder name
            String title = FileUtils.toFileName(folderPath);
            // TODO: update title ....
        }

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


        // add a test label to graph region
        Label label = new Label();
        graphPane.setTop(label);
        // show the detail of the data folder
        String message = "FolderPath: " + folderPath;
        File folder = new File(folderPath);
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            message += "\n" + file.getName();
        }
        // show message
        label.setText(message);

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
