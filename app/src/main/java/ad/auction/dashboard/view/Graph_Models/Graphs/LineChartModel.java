package ad.auction.dashboard.view.Graph_Models.Graphs;


import javafx.scene.chart.AreaChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;



public class LineChartModel extends ChartModel {

    // Set of data points
    private final HashMap<String, List<Point2D>> datasets = new HashMap<>();

    private Boolean clicked = false;

    public LineChartModel(String titleName, String xAxisName, String yAxisName) {
        super(titleName, xAxisName, yAxisName);
    }

    public void clearDatasets() {
        this.datasets.clear();
    }

    public void addDataset(String name, List<Point2D> data) {
        this.datasets.put(name, data);
    }


    private void setAllSeries(ArrayList<XYChart.Series<Number, Number>> seriesList) {
        datasets.forEach((name, data) -> {
            var dataSeries = new XYChart.Series<Number, Number>();
            if (data == null) {
                return;
            }

            data.forEach((p) -> dataSeries.getData().add(new XYChart.Data<Number,Number>(p.getX(), p.getY())));
            seriesList.add(dataSeries);
            dataSeries.setName(name);
            
        });

    }

    public StackPane getLineChart() {
        var xAxis = new NumberAxis();
        xAxis.setLabel(getXName());

        var yAxis = new NumberAxis();
        yAxis.setLabel(getYName());

        
        var allData = new ArrayList<XYChart.Series<Number, Number>>();
        //setAllSeries(allData);
        

        var lChart = new LineChart<>(xAxis, yAxis);
        //lChart.setTitle(getTitleName());

        setAllSeries(allData);
        lChart.getData().addAll(allData);

        //lChart.getData().addAll(allData);
        //lChart.setCreateSymbols(false);


        var label = new Label();
        label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        var pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(lChart, label);
        StackPane.setAlignment(label, Pos.BOTTOM_LEFT);
        StackPane.setMargin(label, new Insets(25));

        createCursorMonitor(lChart, label);
        return pane;

    }

    public StackPane histogram(String xAxisLabel, List<Point2D> data) {
        var xAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);

        var yAxis = new NumberAxis();
        yAxis.setLabel("frequency");

        var series = new XYChart.Series<Number, Number>();
        series.setName(this.getTitleName());
        data.forEach((p) -> series.getData().add(new XYChart.Data<Number,Number>(p.getX(), p.getY())));

        var aChart = new AreaChart<>(xAxis, yAxis);
        aChart.getData().add(series);
        aChart.setCreateSymbols(false);

        var label = new Label();
        label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        var pane = new StackPane();
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(aChart, label);
        StackPane.setAlignment(label, Pos.BOTTOM_LEFT);
        StackPane.setMargin(label, new Insets(25));

        createCursorMonitor(aChart, label);
        return pane;
    }
    
    private String getFormat(Number num) {
        return num.doubleValue() < 1 ? "%.3f" : "%.2f";
    }

    private void createCursorMonitor(XYChart<Number, Number> chart, Label label) {

        final var x = chart.getXAxis();
        final var y = chart.getYAxis();

        var xDash = new XYChart.Series<Number, Number>();
        var yDash = new XYChart.Series<Number, Number>();


        final Node backgroundNodes = chart.lookup(".chart-plot-background");

        
        for (Node node : backgroundNodes.getParent().getChildrenUnmodifiable()) {
            if (node != backgroundNodes && node != x && node != y ) {
                node.setMouseTransparent(true);

            }
        }

        backgroundNodes.setOnMouseClicked((ev) -> {
            label.setVisible(true);
            if (clicked) {
                //noinspection unchecked
                chart.getData().removeAll(xDash, yDash);
                xDash.getData().clear();
                yDash.getData().clear();
            } else {
                var xVal = x.getValueForDisplay(ev.getX());
                var yVal = y.getValueForDisplay(ev.getY());

                xDash.getData().add(new XYChart.Data<Number,Number>(xVal, 0.0));
                xDash.getData().add(new XYChart.Data<Number,Number>(xVal, yVal));
                xDash.setName(String.format("x = " + getFormat(xVal), xVal));
                

                yDash.getData().add(new XYChart.Data<Number,Number>(0.0, yVal));
                yDash.getData().add(new XYChart.Data<Number,Number>(xVal, yVal));
                yDash.setName(String.format("y = " + getFormat(yVal), yVal));
                

                chart.getData().addAll(xDash, yDash);
                xDash.getNode().setStyle("-fx-stroke-dash-array: 2 12 12 2; filter: brightness(25%); -fx-stroke: #5999ce;");
                yDash.getNode().setStyle("-fx-stroke-dash-array: 2 12 12 2; filter: brightness(25%); -fx-stroke: #5999ce;");
                 
            }

            clicked = !clicked;
        });

        //Coordinates of Nodes
        backgroundNodes.setOnMouseMoved((ev) -> {
            label.setVisible(true);
            var xVal = x.getValueForDisplay(ev.getX());
            var yVal = y.getValueForDisplay(ev.getY());
            var formatX = "(" + getFormat(xVal) + ",";
            var formatY = getFormat(yVal) + ")";
            label.setText(String.format(formatX + formatY, xVal, yVal));


        });
        backgroundNodes.setOnMouseExited((ev) -> label.setVisible(false));
        

        //Coordinates in x axis
        x.setOnMouseMoved((ev) -> {
            label.setVisible(true);
            var currentValue = x.getValueForDisplay(ev.getX());
            final var format = "x = " + getFormat(currentValue);
            label.setText(String.format(format, currentValue));

        });
        x.setOnMouseExited((ev) -> label.setVisible(false));

        //Coordinates in y axis
        y.setOnMouseMoved((ev) -> {
            label.setVisible(true);
            var currentValue = y.getValueForDisplay(ev.getY());
            final var format = "y = " + getFormat(currentValue);
            label.setText(String.format(format, currentValue));
        });
        y.setOnMouseExited((ev) -> label.setVisible(false));

    }

}


