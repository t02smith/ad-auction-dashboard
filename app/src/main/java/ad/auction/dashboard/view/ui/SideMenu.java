package ad.auction.dashboard.view.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * The side menu on each page wrapped in a VBox with buttons
 * @author hhg1u20
 */
public class SideMenu extends ScrollPane {
	
    private static final Logger logger = LogManager.getLogger(SideMenu.class.getSimpleName());
	
	private Window window;
	
	public SideMenu(Window window) { 
		this.window = window; 
		
		//Remove the scrollbar for the side menu
		this.setHbarPolicy(ScrollBarPolicy.NEVER);
		this.setVbarPolicy(ScrollBarPolicy.NEVER);
		
		createSideMenu();
	}
	
	private void createSideMenu() {
		logger.info("Creating Side Menu");
		
		//The whole menu is a VBox that is scrollable, so VBox is the child of this ScrollPane
		var vbox = new VBox();
		vbox.getStyleClass().add("sideBackground");
		this.setContent(vbox);
		
		//Buttons in the VBox
		var numberClicksButton = new Button("Number of clicks");
	    var numberUniqButton = new Button("Number of uniques");
	    var numberConvButton = new Button("Number of conversions");
	    var moneySpentButton = new Button("Money spent");
        var ctrButton = new Button("CTR");
        var cpaButton = new Button("CPA");
        var cpmButton = new Button("CPM");
        var bounceRateButton = new Button("Bounce rate");
        var clickCostButton = new Button("Click cost");
        var printButton = new Button("Print");
	        
        vbox.getChildren().addAll(numberClicksButton,numberUniqButton,numberConvButton,
        		moneySpentButton,ctrButton,cpaButton,cpmButton,bounceRateButton,clickCostButton,printButton);
	    
        //Different plot categories for each button
        numberClicksButton.setOnMouseClicked((e) -> window.updateAdvertPage(0));
        numberUniqButton.setOnMouseClicked((e) -> window.updateAdvertPage(1));
        numberConvButton.setOnMouseClicked((e) -> window.updateAdvertPage(2));
        moneySpentButton.setOnMouseClicked((e) -> window.updateAdvertPage(3));
        ctrButton.setOnMouseClicked((e) -> window.updateAdvertPage(4));
        cpaButton.setOnMouseClicked((e) -> window.updateAdvertPage(5));
        cpmButton.setOnMouseClicked((e) -> window.updateAdvertPage(6));
        bounceRateButton.setOnMouseClicked((e) -> window.updateAdvertPage(7));
        clickCostButton.setOnMouseClicked((e) -> window.updateAdvertPage(8));
        printButton.setOnMouseClicked((e) -> window.updateAdvertPage(9));
        
        //Style each button
        var iter = vbox.getChildren().iterator();
        
        while (iter.hasNext()) {
        	var currentButton = iter.next();
        	VBox.setMargin(currentButton, new Insets(10, 10, 10, 10));
        	currentButton.getStyleClass().add("buttonStyle");
        }
	}
}
