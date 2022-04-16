package ad.auction.dashboard.view.pages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import javax.swing.plaf.metal.MetalBorders.TableHeaderBorder;

import com.google.common.collect.Table;

import ad.auction.dashboard.model.calculator.Histogram;
import ad.auction.dashboard.model.calculator.MetricManager;
import ad.auction.dashboard.model.campaigns.Campaign;
import ad.auction.dashboard.view.components.ButtonList;
import ad.auction.dashboard.view.components.FilterMenu;
import ad.auction.dashboard.view.components.TabMenu;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.text.Text;
import javafx.util.Callback;

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
    private StackPane dashBoard = new StackPane();
    private final BorderPane screen = new BorderPane();
    private TableView<MetricManager> metricBox;

    private Metrics currentMetric = DEFAULT_METRIC;

    private final Button histogramToggle = new Button("Histogram");
    private final Button dashButton = new Button("Dashboard");
    private boolean histogramActive = false;
    private boolean cumulative = false;
    private boolean onDashboard = false;

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
        editButton.setPrefHeight(40);
        editButton.setPrefWidth(150);
        BorderPane.setAlignment(editButton, Pos.CENTER);
        editButton.getStyleClass().add("buttonStyle");
        editButton.setOnMouseClicked((e) -> window.openEditPage(campaignName, window::startMenu));

        //DashBoard toggle button
        dashButton.setPrefHeight(40);
        dashButton.setPrefWidth(150);
        dashButton.getStyleClass().add("buttonStyle");


        //cumulative toggle
        var cumToggle = new Button("Cumulative");
        cumToggle.setPrefHeight(40);
        cumToggle.setPrefWidth(150);
        cumToggle.getStyleClass().add("buttonStyle");
        cumToggle.setOnAction(e -> {
            cumulative = !cumulative;
            controller.setCumulative(cumulative);
            cumToggle.setText(cumulative ? "Per Day": "Cumulative");
            loadMetric.accept(currentMetric);
        });

        var right = new HBox(histogramToggle, cumToggle, dashButton, editButton);
        BorderPane.setMargin(right, new Insets(0, 3 ,0, 0));
        right.setSpacing(25);
        right.setAlignment(Pos.CENTER);

        histogramToggle.setPrefHeight(40);
        histogramToggle.setPrefWidth(150);
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

        
        dashButton.setText("Dashboard");
        onDashboard = false;
        dashButton.setOnAction(e -> {
            if (onDashboard) {
                logger.info("Changing to Linechart");
                onDashboard = false;
                dashButton.setText("Dashboard");
                loadMetric.accept(currentMetric);
            } else {
                logger.info("Loading Dashboard");
                onDashboard = true;
                dashButton.setText("Graph");
                this.screen.setCenter(this.dashBoard);
                dashBoard.getChildren().add(buildDashBoard());

            }
        });

    }

    private BorderPane buildDashBoard() {
        var dashPane = new BorderPane();
        dashPane.getStyleClass().setAll("bg-primary");

        var dTitle = new Label("Dashboard");
        dTitle.setAlignment(Pos.CENTER);
        dTitle.getStyleClass().setAll("topTitle");

        dashPane.setTop(dTitle);
        BorderPane.setAlignment(dTitle, Pos.CENTER);


        this.metricBox = new TableView<>();
        metricBox.getStyleClass().add("table-view");

        metricBox.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn metricColumn = new TableColumn<MetricManager, String>("Metrics");
        metricColumn.setCellValueFactory(new PropertyValueFactory<MetricManager, String>("currentMetric"));
        
        metricBox.getColumns().add(metricColumn);

        var impCountManager = new MetricManager(this.controller, Metrics.IMPRESSION_COUNT);
        var clkCountManager = new MetricManager(this.controller, Metrics.CLICK_COUNT);
        var unqCountManager = new MetricManager(this.controller, Metrics.UNIQUES_COUNT);
        var bounceCountManager = new MetricManager(this.controller, Metrics.BOUNCES_COUNT);
        var convCountManager = new MetricManager(this.controller, Metrics.CONVERSIONS_COUNT);
        var impCostManager = new MetricManager(this.controller, Metrics.TOTAL_COST_IMPRESSION);
        var clkCostManager = new MetricManager(this.controller, Metrics.TOTAL_COST_CLICK);
        var tCostManager = new MetricManager(this.controller, Metrics.TOTAL_COST);
        var ctrManager = new MetricManager(this.controller, Metrics.CTR);
        var cpaManager = new MetricManager(this.controller, Metrics.CPA);
        var cpcManager = new MetricManager(this.controller, Metrics.CPC);
        var cpmManager = new MetricManager(this.controller, Metrics.CPM);
        var bounceRateManager = new MetricManager(this.controller, Metrics.BOUNCE_RATE);

        var activeCampaigns = impCountManager.getActiveCampaigns();

        activeCampaigns.forEach((camp) -> {
            TableColumn campColumn = new TableColumn<MetricManager, String>(camp);
            campColumn.setCellValueFactory(new Callback<CellDataFeatures<MetricManager, String>, ObservableValue<String>>() {

                @Override 
                public ObservableValue<String> call(CellDataFeatures<MetricManager, String> rowObject) { 
                    return new SimpleStringProperty(rowObject.getValue().getValue(camp)); 
                } 
            });

            metricBox.getColumns().add(campColumn);
        });

        metricBox.getItems().add(impCountManager);
        metricBox.getItems().add(clkCountManager);
        metricBox.getItems().add(unqCountManager);
        metricBox.getItems().add(bounceCountManager);
        metricBox.getItems().add(convCountManager);
        metricBox.getItems().add(impCostManager);
        metricBox.getItems().add(clkCostManager);
        metricBox.getItems().add(tCostManager);
        metricBox.getItems().add(ctrManager);
        metricBox.getItems().add(cpaManager);
        metricBox.getItems().add(cpcManager);
        metricBox.getItems().add(cpmManager);
        metricBox.getItems().add(bounceRateManager);


        
        restrictDashboardHeight();
        dashPane.setCenter(metricBox);

        return dashPane;

    }

    private void restrictDashboardHeight() {

        metricBox.setFixedCellSize(25);
        metricBox.setPadding(new Insets(0, 20, 15, 20));

        int rows = metricBox.getItems().size();
        logger.info(rows);
        TableHeaderRow headRow = (TableHeaderRow) metricBox.lookup("TableHeaderRow");
        double height = (rows * metricBox.getFixedCellSize())
                      + metricBox.getInsets().getTop() + metricBox.getInsets().getBottom()
                      + (headRow == null ? 0 : headRow.getHeight());
        logger.info(height);

        metricBox.setMinHeight(height);
        metricBox.setMaxHeight(height+metricBox.getFixedCellSize());
        metricBox.setPrefHeight(height);
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
