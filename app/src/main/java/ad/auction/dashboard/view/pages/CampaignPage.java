package ad.auction.dashboard.view.pages;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.view.Graph_Models.Graphs.LineChartModel;
import ad.auction.dashboard.view.components.MetricSelection;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Advertisement page that holds the UI part of the graphs and controls
 *
 * @author tcs1g20, hhg1u20
 */
public class CampaignPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CampaignPage.class.getSimpleName());
    private final Controller controller = App.getInstance().controller();

    private final String campaignName;

    private LineChartModel graph;
    private BorderPane screen = new BorderPane();

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
        this.graph = new LineChartModel(campaignName, "Time (days)", "");

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

        root.getChildren().add(screen);

        //Title background on top
        var title = new BorderPane();
        title.getStyleClass().add("topBackground");

        //BorderPane to hold the graph and the buttons under it
        var graphPane = new BorderPane();

        //HBox to hold the buttons below the graph
        var graphButtonPane = new HBox();



        //Back button
        var backButton = new Button("<");
        backButton.getStyleClass().add("buttonStyle");
        backButton.setOnMouseClicked((e) -> window.startMenu());

        //Title text on top
        var mainMenuText = new Text(campaignName);
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);

        graphPane.setBottom(graphButtonPane);
        graphPane.setCenter(graph.getLineChart());

        screen.setTop(title);
        screen.setLeft(this.metricSelection());
        screen.setRight(backButton);
        screen.setCenter(graphPane);

        //Style the buttons under the graph
        styleButtons(graphButtonPane);
    }

    @SuppressWarnings("unchecked")
    private MetricSelection metricSelection() {
        return new MetricSelection(m -> {
            Future<Object> future = controller.runCalculation(m, MetricFunction.OVER_TIME);
            while (!future.isDone()) {}

            try {
                var data = (List<Point2D>)future.get();

                this.graph.setData(data);
                this.graph.setTitleName(m.getMetric().displayName());
                this.graph.setYName(m.getMetric().displayName());
                this.screen.setCenter(this.graph.getLineChart());
                
            } catch (InterruptedException | ExecutionException e) {
                //
            }
            
        });
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
