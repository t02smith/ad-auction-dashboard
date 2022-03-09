package ad.auction.dashboard.view.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Main Menu page that holds the UI elements for its controls
 * @author hhg1u20
 *
 */
public class MenuPage extends BasePage {
	
    private static final Logger logger = LogManager.getLogger(MenuPage.class.getSimpleName());

	public MenuPage(Window window) {
		super(window);
		logger.info("Creating Menu Page");
	}

	/**
	 * Builds the UI elements on the page
	 */
	@Override
	public void build() {
		logger.info("Building Main Menu");
		
		//The root is a stack pane	
		root = new StackPane();
        root.setMaxWidth(window.getWidth());
        root.setMaxHeight(window.getHeight());
        
        //Root holds Border Pane, Border Pane holds everything else
        var menuPane = new BorderPane();
        root.getChildren().add(menuPane);
        
        //Title background on top
        var title = new BorderPane();
        title.getStyleClass().add("topBackground");        
        
        //Title text on top
        var mainMenuText = new Text("Main Menu");
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);
        
        //Side menu on the left
        var sideMenu = new VBox();
        sideMenu.getStyleClass().add("sideBackground");

        //Buttons in the side menu
        var rangeDateButton = new Button("Range by date");
        var rangeSegmentButton = new Button("Range by segment");
        sideMenu.getChildren().addAll(rangeDateButton,rangeSegmentButton);
        
        //Add style to each button
        //TODO: Figure out why the Font is not working
        var iter = sideMenu.getChildren().iterator();
        
        while (iter.hasNext()) {
        	var currentButton = iter.next();
        	VBox.setMargin(currentButton, new Insets(10, 10, 10, 10));
        	currentButton.getStyleClass().add("buttonStyle");
        }
     
        //TODO: Add functionality to the RANGE BY buttons
        
        //FlowPane to add each advertisement in
        var flowPane = new FlowPane();
        flowPane.setPadding(new Insets(5, 0, 5, 0));
        flowPane.setVgap(30);
        flowPane.setHgap(60);
        flowPane.setPrefWrapLength(500);
        
        /**
         * Add each advertisement to the FlowPane (i < 11 is currently a placeholder)
         * TODO: Change the boundary of the loop to the number of actual advertisements
         */
        for (int i = 1; i < 11; i++) {
        	var advertButton = new Button("Advertisement " + i);
        	advertButton.getStyleClass().add("advertButton");
        	advertButton.setOnMouseClicked((e) -> window.openAdvertPage(0)); //TODO: Pass graphs
        	flowPane.getChildren().add(advertButton);
        }
        
        //Scroll pane to hold the flow pane and enable scrolling
        var scrollPane = new ScrollPane();
        scrollPane.setContent(flowPane);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        
        menuPane.setTop(title);
        menuPane.setLeft(sideMenu);
        menuPane.setCenter(scrollPane);
        BorderPane.setMargin(scrollPane, new Insets(50, 0, 0, 200));
	}

	@Override
	public void update(int category) {}

}
