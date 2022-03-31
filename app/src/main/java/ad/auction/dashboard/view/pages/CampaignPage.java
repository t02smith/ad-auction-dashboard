package ad.auction.dashboard.view.pages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import ad.auction.dashboard.model.calculator.Histogram;
import ad.auction.dashboard.model.campaigns.Campaign;
import ad.auction.dashboard.view.components.ButtonList;
import ad.auction.dashboard.view.components.FilterMenu;
import ad.auction.dashboard.view.components.TabMenu;
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

    public static final Metrics DEFAULT_METRIC = Metrics.IMPRESSION_COUNT;

    private static final Logger logger = LogManager.getLogger(CampaignPage.class.getSimpleName());
    private final Controller controller = App.getInstance().controller();

    private String campaignName;

    private LineChartModel graph;
    private final BorderPane screen = new BorderPane();

    private Metrics currentMetric = DEFAULT_METRIC;

    private final Button histogramToggle = new Button("Histogram");
    private boolean histogramActive = false;

    //Function to load a given metric
    @SuppressWarnings("unchecked")
    private final Consumer<Metrics> loadMetric = m -> {
        this.setMetric(m);
        this.graph.setYName(m.getMetric().unit());

        HashMap<String, Future<Object>> future = controller.runCalculation(m, MetricFunction.OVER_TIME);
        while (future.values().stream().anyMatch(f -> !f.isDone())) {}

        future.forEach((name, data) -> {
            try {this.graph.addDataset(name, (List<Point2D>) data.get());}
            catch (Exception ignored) {}
        });

        this.screen.setCenter(this.graph.getLineChart());

        
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

        //BorderPane to hold the graph and the buttons under it
        var graphPane = new BorderPane();
        graphPane.getStyleClass().add("bg-primary");

        //HBox to hold the buttons below the graph
        var graphButtonPane = new HBox();

        var rightMenu = new VBox();
        rightMenu.setAlignment(Pos.TOP_CENTER);
        rightMenu.setSpacing(10);

        var filterTitle = new Text("Filters");
        filterTitle.getStyleClass().add("filter-title");
        var filterMenu = new FilterMenu(() -> this.loadMetric.accept(this.currentMetric), cData.start().toLocalDate(), cData.end().toLocalDate());
        rightMenu.getChildren().addAll(filterTitle, filterMenu);

        graphPane.setBottom(graphButtonPane);
        graphPane.setCenter(graph.getLineChart());

        // Tab menu for metrics + filters
        var menu = new TabMenu("metrics");
        menu.addPane("metrics", new ButtonList<Metrics>(Arrays.asList(Metrics.values()),DEFAULT_METRIC,false,loadMetric));
        menu.addPane("filters", filterMenu);
        menu.addPane("compare", campaignList());
        menu.build();

        screen.setTop(title());
        screen.setLeft(menu);
        screen.setCenter(graphPane);
        this.loadMetric.accept(this.currentMetric);
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

    private BorderPane title() {
        //Title background on top
        var title = new BorderPane();
        title.getStyleClass().add("topBackground");

        //Title text on top
        var mainMenuText = new Text(campaignName);
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);

        //Add back button
        var backButton = buildBackButton();
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(0, 0 ,0, 3));
        title.setLeft(backButton);

        //Settings page button
        var editButton = new Button("Settings");
        BorderPane.setAlignment(editButton, Pos.CENTER);
        editButton.getStyleClass().add("buttonStyle");
        editButton.setOnMouseClicked((e) -> window.openEditPage(campaignName, window::startMenu));

        var right = new HBox(histogramToggle, editButton);
        right.setSpacing(25);
        right.setAlignment(Pos.CENTER);

        histogramToggle.setVisible(false);
        histogramToggle.getStyleClass().add("buttonStyle");
        title.setRight(right);

        return title;
    }

    @SuppressWarnings("unchecked")
    private void setMetric(Metrics m) {
        this.currentMetric = m;
        if (this.currentMetric.getMetric() instanceof Histogram) {
            histogramToggle.setOnAction(e -> {
                if (histogramActive) {
                    this.loadMetric.accept(this.currentMetric);
                    this.histogramToggle.setText("Histogram");
                    histogramActive = false;
                    return;
                }

                logger.info("Loading histogram");
                histogramActive = true;
                this.histogramToggle.setText("Line");
                HashMap<String, Future<Object>> future = controller.runCalculation(m, MetricFunction.HISTOGRAM);
                while (future.values().stream().anyMatch(f -> !f.isDone())) {}

                try {
                    var data = (List<Point2D>)future.get(campaignName).get();
                    this.graph.setTitleName(m.getMetric().displayName());
                    this.screen.setCenter(this.graph.histogram(this.currentMetric.getMetric().unit(), data));

                } catch (Exception ignored) {}


            });
            histogramToggle.setVisible(true);

        } else {
            histogramToggle.setVisible(false);
        }
    }

    private ButtonList<String> campaignList() {
        var cList = controller.getCampaigns().stream()
                .map(Campaign.CampaignData::name)
                .filter(c -> !c.equals(campaignName))
                .toList();

        return new ButtonList<>(cList, null, true, c -> {
            var res = controller.toggleCampaign(c);
            while (!res.isDone()) {}
            loadMetric.accept(currentMetric);
        });
    }
}
