package ad.auction.dashboard.view.Graph_Models.Graphs;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LineChartModel extends ChartModel {

    // Set of data points
    private ArrayList<Point2D> data;

    public LineChartModel(String titleName, String xAxisName, String yAxisName) {
        super(titleName, xAxisName, yAxisName);
    }

    public ArrayList<Point2D> getData() {
        return data;
    }

    public void setData(ArrayList<Point2D> newData) {
        this.data = newData;
    }

    private void setSeries(XYChart.Series<Number, Number> dataSeries) {
        if (data == null)
            return;

        data.forEach(p -> dataSeries.getData().add(new XYChart.Data<>(p.getX(), p.getY())));
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
        lChart.setTitle(getTitleName());
        lChart.getData().add(dataSeries);

        return lChart;

    }
}
