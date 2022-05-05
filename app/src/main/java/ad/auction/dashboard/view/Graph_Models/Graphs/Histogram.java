package ad.auction.dashboard.view.Graph_Models.Graphs;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.Point2D;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Builds the Histogram
 */
public class Histogram extends ChartModel{

    private List<Point2D> data;

    private static final Logger logger = LogManager.getLogger(LineChartModel.class.getSimpleName());

    public Histogram(String titleName, String xAxisName, String yAxisName) {
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
    
}
