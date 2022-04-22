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
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger logger = LogManager.getLogger(CampaignPage.class.getSimpleName());

    private final String campaignName;

    private LineChartModel graph;
    private final BorderPane screen = new BorderPane();
    private final VBox selectionWrapper = new VBox();

    //current selection
    private Metrics currentMetric = controller.getDefaultMetric();
    private CampaignComponent active = CampaignComponent.CUMULATIVE_GRAPH;

    private enum CampaignComponent {
        CUMULATIVE_GRAPH("Cumulative"),
        TREND_GRAPH("Trend"),
        HISTOGRAM("Histogram");
        //Dashboard tbd

        private final String displayName;

        CampaignComponent(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    //Function to load a given metric
    @SuppressWarnings("unchecked")
    private final Consumer<Metrics> loadMetric = m -> {
        this.currentMetric = m;
        this.graph.clearDatasets();
        this.graph.setYName(m.getMetric().unit());

        if (!(this.currentMetric.getMetric() instanceof Histogram) && this.active == CampaignComponent.HISTOGRAM) {
            this.active = CampaignComponent.CUMULATIVE_GRAPH;
        }

        var newSelection = campaignComponents();
        this.selectionWrapper.getChildren().clear();
        this.selectionWrapper.getChildren().add(newSelection);

        HashMap<String, Future<Object>> future = controller.runCalculation(m, switch (active) {
            case HISTOGRAM -> MetricFunction.HISTOGRAM;
            case CUMULATIVE_GRAPH, TREND_GRAPH -> MetricFunction.OVER_TIME;
        });
        while (future.values().stream().anyMatch(f -> !f.isDone())) {}

        future.forEach((name, data) -> {
            try {
                this.graph.addDataset(name, (List<Point2D>) data.get());
                this.screen.setCenter(
                        (active == CampaignComponent.HISTOGRAM)
                                ? this.graph.histogram(this.currentMetric.getMetric().unit(), (List<Point2D>)data.get())
                                : this.graph.getLineChart()
                );
            }
            catch (Exception ignored) {}
        });
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
        menu.addPane("metrics", new ButtonList<>(Arrays.asList(Metrics.values()),this.currentMetric,false,loadMetric));
        menu.addPane("filters", filterMenu);
        menu.addPane("compare", campaignList());
        menu.build();

        this.selectionWrapper.setAlignment(Pos.CENTER);

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
        var editButton = new Button("Edit");
        BorderPane.setAlignment(editButton, Pos.CENTER);
        editButton.getStyleClass().add("buttonStyle");
        editButton.setOnMouseClicked((e) -> window.openEditPage(campaignName, window::startMenu));

        selectionWrapper.getChildren().add(campaignComponents());
        var right = new HBox(selectionWrapper, editButton);
        right.setSpacing(25);
        right.setAlignment(Pos.CENTER);

        title.setRight(right);
        return title;
    }

    private VBox campaignList() {
        var emptyMsg = new Label("No active snapshots");
        emptyMsg.getStyleClass().addAll("bg-secondary");

        var genSnapshot = new Button("Snapshot");
        var hbox = new HBox(genSnapshot);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));
        hbox.getStyleClass().add("bg-secondary");

        //List of active included/joined campaigns
        var active = new VBox(hbox);
        active.setMinWidth(300);
        active.getStyleClass().add("bg-primary");

        int[] counter = new int[] {1};

        genSnapshot.getStyleClass().add("buttonStyle");
        genSnapshot.setOnAction(e -> {
            var id = controller.snapshot();
            var btn = new Label("Snapshot " + counter[0]);
            counter[0] += 1;
            btn.getStyleClass().add("metric-btn");
            btn.setMinWidth(300);
            btn.setOnMouseClicked(ev -> {
                controller.removeSnapshot(id);
                active.getChildren().remove(btn);
                if (active.getChildren().size() == 1)
                    active.getChildren().add(emptyMsg);
            });

            if (active.getChildren().size()==1)
                active.getChildren().remove(emptyMsg);
            active.getChildren().add(btn);
        });

        var cList = controller.getCampaigns().stream()
                .map(Campaign.CampaignData::name)
                .filter(c -> !c.equals(campaignName))
                .toList();

        var bl = new ButtonList<>(cList, null, true, c -> {
            var res = controller.toggleCampaign(c);
            while (!res.isDone()) {}
            loadMetric.accept(currentMetric);
        });

        return new VBox(bl, active);
    }

    private ComboBox<CampaignComponent> campaignComponents() {
        var options = new ComboBox<>(FXCollections.observableArrayList(
                this.currentMetric.getMetric() instanceof Histogram
                ? CampaignComponent.values()
                : new CampaignComponent[] {CampaignComponent.CUMULATIVE_GRAPH, CampaignComponent.TREND_GRAPH}));
        options.setValue(this.active);
        options.valueProperty().addListener(e -> {
            this.active = options.getValue();
            switch (this.active) {
                case CUMULATIVE_GRAPH -> controller.setCumulative(true);
                case TREND_GRAPH -> controller.setCumulative(false);
            }

            loadMetric.accept(currentMetric);
        });

        return options;
    }
}
