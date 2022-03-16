package ad.auction.dashboard.view.pages;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import ad.auction.dashboard.view.components.FilterMenu;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.view.Graph_Models.Graphs.LineChartModel;
import ad.auction.dashboard.view.components.MetricSelection;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
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

    private Metrics currentMetric = Metrics.IMPRESSION_COUNT;

    //Function to load a given metric
    @SuppressWarnings("unchecked")
    private final Consumer<Metrics> loadMetric = m -> {
        this.currentMetric = m;

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
        
    };


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
        var cData = controller.getCampaignData();
        logger.info("Building Campaign page for {}", campaignName);

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

        var rightMenu = new VBox();
        rightMenu.setAlignment(Pos.TOP_CENTER);
        rightMenu.setSpacing(10);

        var filterTitle = new Text("Filters");
        filterTitle.getStyleClass().add("filter-title");
        var filterMenu = new FilterMenu(() -> this.loadMetric.accept(this.currentMetric), cData.start().toLocalDate(), cData.end().toLocalDate());
        rightMenu.getChildren().addAll(filterTitle, filterMenu);

        //Title text on top
        var mainMenuText = new Text(campaignName);
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);

        // Add back button
        var backButton = buildBackButton();
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(0, 0 ,0, 3));
        title.setLeft(backButton);

        graphPane.setBottom(graphButtonPane);
        graphPane.setCenter(graph.getLineChart());

        screen.setTop(title);
        screen.setLeft(new MetricSelection(loadMetric));
        screen.setRight(rightMenu);
        screen.setCenter(graphPane);

        //Style the buttons under the graph
        styleButtons(graphButtonPane);
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

    /**
     * Build the back button
     * @return back button
     */
    private StackPane buildBackButton() {
        //Create the button
        var backButton = new Button();
        backButton.getStyleClass().add("buttonStyle");
        backButton.setMaxWidth(50);
        backButton.setMaxHeight(40);
        var buttonWidth = backButton.getMaxWidth();
        var buttonHeight = backButton.getMaxHeight();
        backButton.setOnMouseClicked((e) -> window.startMenu());


        // Draw arrow with canvas
        var canvas = new Canvas(buttonWidth, buttonHeight);
        canvas.setMouseTransparent(true);

        var gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.strokeLine(buttonWidth * 0.25, buttonHeight * 0.5, buttonWidth * 0.75,  buttonHeight * 0.5);
        gc.strokeLine(buttonWidth * 0.25, buttonHeight * 0.5, buttonWidth * 0.5, buttonHeight * 0.725);
        gc.strokeLine(buttonWidth * 0.25, buttonHeight * 0.5, buttonWidth * 0.5, buttonHeight * 0.275);

        // Stack the drawn arrow on the button
        var pane = new StackPane();
        pane.getChildren().addAll(backButton, canvas);

        return pane;
    }


}
