package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.model.calculator.Histogram;
import ad.auction.dashboard.model.calculator.MetricManager;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.calculator.calculations.Metric.MetricFunction;
import ad.auction.dashboard.model.campaigns.Campaign;
import ad.auction.dashboard.view.Graph_Models.Graphs.LineChartModel;
import ad.auction.dashboard.view.components.BackBtn;
import ad.auction.dashboard.view.components.ButtonList;
import ad.auction.dashboard.view.components.FilterList;
import ad.auction.dashboard.view.components.TabMenu;
import ad.auction.dashboard.view.ui.Window;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Advertisement page that holds the UI part of the graphs and controls
 *
 * @author tcs1g20, hhg1u20
 */
public class CampaignPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(CampaignPage.class.getSimpleName());

    //The campaign's name
    private final String campaignName;

    //The graph to be displayed
    private LineChartModel graph;

    //The dashboard of overall values
    private final StackPane dashBoard = new StackPane();
    private TableView<MetricManager> metricBox;

    private final BorderPane screen = new BorderPane();
    private final VBox selectionWrapper = new VBox();


    //current selection
    private Metrics currentMetric = controller.getDefaultMetric();
    private CampaignComponent active = CampaignComponent.CUMULATIVE_GRAPH;

    private final Button dashButton = new Button("Dashboard");
    private boolean onDashboard = false;

    /**
     * The graph to be displayed
     */
    private enum CampaignComponent {
        CUMULATIVE_GRAPH("Cumulative"),
        TREND_GRAPH("Trend"),
        HISTOGRAM("Histogram");

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

    /**
     * Create a new CampaignPage
     * @param window the window the page is on
     * @param campaignName the name of the campaign to show
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
        var filterMenu = new FilterList(
                () -> this.loadMetric.accept(this.currentMetric)
                ,() -> controller.toggleAllFilters(false)
                , cData.start()
                , cData.end());
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
     * Create the title bar for the campaign
     * @return campaign title bar w/ option buttons
     */
    private BorderPane title() {
        //Title background on top
        var title = new BorderPane();
        title.getStyleClass().add("topBackground");

        //Title text on top
        var mainMenuText = new Text(campaignName);
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);

        //Add back button
        var backButton = new BackBtn(e -> {
            controller.toggleAllFilters(false);
            window.startMenu();
        });
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(backButton, new Insets(0, 0 ,0, 3));
        title.setLeft(backButton);

        //Settings page button
        var editButton = new Button("Edit");
        BorderPane.setAlignment(editButton, Pos.CENTER);
        editButton.getStyleClass().add("buttonStyle");
        editButton.setOnMouseClicked((e) -> window.openEditPage(campaignName, window::startMenu));

        var snapshot = new Button("Screenshot");
        snapshot.getStyleClass().add("buttonStyle");
        snapshot.setOnMouseClicked(e -> this.graph.screenshot());

        selectionWrapper.getChildren().add(campaignComponents());
        var right = new HBox(selectionWrapper, editButton, snapshot);
        right.setSpacing(25);
        right.setAlignment(Pos.CENTER);

        //DashBoard toggle button
        dashButton.setPrefHeight(40);
        dashButton.setPrefWidth(150);
        dashButton.getStyleClass().add("buttonStyle");
        dashButton.setText("Dashboard");
        onDashboard = false;
        dashButton.setOnAction(e -> {
            if (onDashboard) {
                logger.info("Changing to Line chart");
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

        right.getChildren().add(dashButton);
        title.setRight(right);
        return title;
    }

    /**
     * Creates the list of loadable campaigns and snapshot manager
     * @return list of loadable campaigns and snapshot manager
     */
    private VBox campaignList() {

        var genSnapshot = new Button("Snapshot");
        var hbox = new HBox(genSnapshot);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10));
        hbox.getStyleClass().add("bg-secondary");

        //List of active included/joined campaigns
        var active = new VBox(hbox);
        active.setMinWidth(300);
        active.getStyleClass().add("bg-primary");

        genSnapshot.getStyleClass().add("buttonStyle");
        genSnapshot.setOnAction(e -> {
            try {
                var id = controller.snapshot();

                var btn = new Label(String.valueOf(id));
                btn.getStyleClass().add("snapshot-btn");
                btn.setMinWidth(250);

                var close = new Button("x");
                close.getStyleClass().add("snapshot-close");
                close.setMinWidth(50);

                var hb = new HBox(btn, close);
                hb.setMinWidth(300);
                hb.getStyleClass().addAll("bg-secondary", "snapshot-box");
                hb.setAlignment(Pos.CENTER);

                close.setOnMouseClicked(ev -> {
                    controller.removeSnapshot(id);
                    active.getChildren().remove(hb);
                    loadMetric.accept(currentMetric);
                });

                active.getChildren().add(hb);
            } catch (IllegalStateException err) {
                //TODO
            }
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

    /**
     * Dashboard to show overall values
     * Will show all active campaigns
     * @return campaign dashboard
     */
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
        
        TableColumn<MetricManager, String> metricColumn = new TableColumn<>("Metrics");
        metricColumn.setCellValueFactory(new PropertyValueFactory<>("currentMetric"));
        
        metricBox.getColumns().add(metricColumn);

        var activeCampaigns = controller.getActiveCampaigns();

        activeCampaigns.forEach((camp) -> {
            TableColumn<MetricManager, String> campColumn = new TableColumn<>(camp.name());
            campColumn.setCellValueFactory(rowObject -> new SimpleStringProperty(rowObject.getValue().getOutput(camp.name())));

            metricBox.getColumns().add(campColumn);
        });

        metricBox.getItems().addAll(
            Stream.of(Metrics.values())
                    .parallel()
                    .map(MetricManager::new).toList()
        );
        
        restrictDashboardHeight();
        dashPane.setCenter(metricBox);

        return dashPane;
    }

    /**
     * Enforces the height of the dashboard
     */
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
                default -> logger.error("err");
            }

            loadMetric.accept(currentMetric);
        });

        return options;
    }
}
