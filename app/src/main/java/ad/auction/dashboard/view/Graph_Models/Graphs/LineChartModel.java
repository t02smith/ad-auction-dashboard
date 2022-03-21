package ad.auction.dashboard.view.Graph_Models.Graphs;


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

        
        data.forEach((p) -> {
            dataSeries.getData().add(new XYChart.Data<Number,Number>(p.getX(), p.getY()));
        });

    }

    public LineChart<Number, Number> getLineChart() {
        var xAxis = new NumberAxis();
        xAxis.setLabel(getXName());

        var yAxis = new NumberAxis();
        //yAxis.setLabel(getYName());

        var dataSeries = new XYChart.Series<Number, Number>();
        dataSeries.setName(this.getTitleName());
        setSeries(dataSeries);

        var lChart = new LineChart<>(xAxis, yAxis);
        //lChart.setTitle(getTitleName());
        lChart.getData().add(dataSeries);

        createCursorMonitor(lChart, dataSeries);
        return lChart;

    }

    private void createCursorMonitor(LineChart<Number, Number> chart, XYChart.Series<Number, Number> series) {

        final var x = chart.getXAxis();
        final var y = chart.getYAxis();

        final Node backgroundNodes = chart.lookup(".chart-plot-background");

        for (Node node : backgroundNodes.getParent().getChildrenUnmodifiable()) {
            if (node != backgroundNodes && node != x && node != y ) {
                node.setMouseTransparent(true);
            }
        }

        //Coordinates of Nodes
        backgroundNodes.setOnMouseMoved((ev) -> {
            var xVal = x.getValueForDisplay(ev.getX());
            var yVal = y.getValueForDisplay(ev.getY());
            var formatX = xVal.doubleValue() < 1 ? "(%.3f," : "(%.2f,";
            var formatY = yVal.doubleValue() < 1 ? " %.3f)" : " %.2f)";
            series.setName(String.format(formatX + formatY, xVal, yVal));
        });
        backgroundNodes.setOnMouseExited((ev) -> {series.setName(this.getTitleName());});
        

        //Coordinates in x axis
        x.setOnMouseMoved((ev) -> {
            var currentValue = x.getValueForDisplay(ev.getX());
            final var format = currentValue.doubleValue() < 1 ? "x = %.3f" : "x = %.2f";
            series.setName(String.format(format, currentValue));
        });
        x.setOnMouseExited((ev) -> {series.setName(this.getTitleName());
        });

        //Coordinates in y axis
        y.setOnMouseMoved((ev) -> {
            var currentValue = y.getValueForDisplay(ev.getY());
            final var format = currentValue.doubleValue() < 1 ? "y = %.3f" : "y = %.2f";
            series.setName(String.format(format, currentValue));
        });
        y.setOnMouseExited((ev) -> {series.setName(this.getTitleName());
        });

    }

}


