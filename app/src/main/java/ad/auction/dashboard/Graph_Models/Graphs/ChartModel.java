package ad.auction.dashboard.Graph_Models.Graphs;

public class ChartModel {

    private String title;
    private String xName;
    private String yName;
    
    /*
    Constructor for the main chart code.
    Constructs a chart object with title for the chart, x axis and y axis. As well as storing the data points to be plotted
     */
    public ChartModel(String titleName, String xAxisName,String yAxisName) {
        this.title = titleName;
        this.xName = xAxisName;
        this.yName = yAxisName;
    }

    public String getTitleName() { return title;}

    public void setTitleName(String name) {this.title = name;}

    public String getXName() { return xName;}

    public void setXName(String name) {this.xName = name;}
    
    public String getYName() { return yName;}

    public void setYName(String name) {this.yName = name;}


}
