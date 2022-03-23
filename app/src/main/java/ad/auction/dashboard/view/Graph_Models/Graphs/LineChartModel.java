package ad.auction.dashboard.view.Graph_Models.Graphs;


import javafx.scene.chart.AreaChart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;



public class LineChartModel extends ChartModel {

    private static final Logger logger = LogManager.getLogger(LineChartModel.class.getSimpleName());

    // Set of data points
    private List<Point2D> data;

    private Boolean clicked = false;

    public LineChartModel(String titleName, String xAxisName, String yAxisName) {
        super(titleName, xAxisName, yAxisName);
    }

    public List<Point2D> getData() {
        logger.info("Retrieving Data for current graph");
        return data;
    }

    public void setData(List<Point2D> newData) {
        logger.info("New data is set");
        this.data = newData;
    }

    private void setSeries(XYChart.Series<Number, Number> dataSeries) {
        if (data == null)
            return;

        data.forEach((p) -> dataSeries.getData().add(new XYChart.Data<Number,Number>(p.getX(), p.getY())));

    }

    public LineChart<Number, Number> getLineChart() {
        var xAxis = new NumberAxis();
        xAxis.setLabel(getXName());

        var yAxis = new NumberAxis();
        yAxis.setLabel(getYName());

        var dataSeries = new XYChart.Series<Number, Number>();
        dataSeries.setName(this.getTitleName());
        setSeries(dataSeries);

        var lChart = new LineChart<>(xAxis, yAxis);
        //lChart.setTitle(getTitleName());
        lChart.getData().add(dataSeries);
        lChart.setCreateSymbols(false);

        createCursorMonitor(lChart, dataSeries);
        return lChart;

    }

    public AreaChart<Number, Number> histogram(String xAxisLabel) {
        var xAxis = new NumberAxis();
        xAxis.setLabel(xAxisLabel);

        var yAxis = new NumberAxis();
        yAxis.setLabel("frequency");

        var series = new XYChart.Series<Number, Number>();
        series.setName(this.getTitleName());
        setSeries(series);

        var aChart = new AreaChart<>(xAxis, yAxis);
        aChart.getData().add(series);
        aChart.setCreateSymbols(false);

        return aChart;
    }
    
    private String getFormat(Number num) {
        return num.doubleValue() < 1 ? "%.3f" : "%.2f";
    }

    private void createCursorMonitor(LineChart<Number, Number> chart, XYChart.Series<Number, Number> series) {

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
            if (clicked) {
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
            var xVal = x.getValueForDisplay(ev.getX());
            var yVal = y.getValueForDisplay(ev.getY());
            var formatX = "(" + getFormat(xVal) + ",";
            var formatY = getFormat(yVal) + ")";
            series.setName(String.format(formatX + formatY, xVal, yVal));


        });
        backgroundNodes.setOnMouseExited((ev) -> {
            series.setName(this.getTitleName());
        });
        backgroundNodes.setOnMouseExited((ev) -> series.setName(this.getTitleName()));
        

        //Coordinates in x axis
        x.setOnMouseMoved((ev) -> {
            var currentValue = x.getValueForDisplay(ev.getX());
            final var format = "x = " + getFormat(currentValue);
            series.setName(String.format(format, currentValue));

        });
        x.setOnMouseExited((ev) -> series.setName(this.getTitleName()));

        //Coordinates in y axis
        y.setOnMouseMoved((ev) -> {
            var currentValue = y.getValueForDisplay(ev.getY());
            final var format = "y = " + getFormat(currentValue);
            series.setName(String.format(format, currentValue));
        });
        y.setOnMouseExited((ev) -> series.setName(this.getTitleName()));

    }

}


