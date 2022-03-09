package ad.auction.dashboard.view.Graph_Models.Graphs;

import java.util.Map;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LineChartModel extends ChartModel{

    private Map<Number, Number> data;

    public LineChartModel(String titleName, String xAxisName, String yAxisName, Map<Number, Number> dataList) {
        super(titleName, xAxisName, yAxisName);
        this.data = dataList;
    }

    public Map<Number, Number> getData() { return data;}

    public void setData(Map<Number, Number> newData) {this.data = newData;}
    
    private void setSeries(XYChart.Series<Number, Number> dataSeries) {

        for (Map.Entry<Number, Number> entry : data.entrySet()) {
            dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
    }

    public LineChart<Number, Number> getLineChart() {
        var xAxis = new NumberAxis();
        xAxis.setLabel(getXName());

        var yAxis = new NumberAxis();
        yAxis.setLabel(getYName());

        var dataSeries = new XYChart.Series<Number, Number>();
        setSeries(dataSeries);

        var lChart = new LineChart<>(xAxis, yAxis);
        lChart.setTitle(getTitleName());
        lChart.getData().add(dataSeries);

        return lChart;
        
    }
}
