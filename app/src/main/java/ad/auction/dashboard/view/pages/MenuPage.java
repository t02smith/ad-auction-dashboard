package ad.auction.dashboard.view.pages;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller.ControllerQuery;
import ad.auction.dashboard.model.campaigns.Campaign.CampaignData;
import ad.auction.dashboard.view.ui.Window;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Main Menu page that holds the UI elements for its controls
 *
 * @author tcs1g20, hhg1u20
 */
public class MenuPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(MenuPage.class.getSimpleName());
    private FlowPane flowPane; // the flow pane contains the campaign buttons
    
    private double sideMenuWidth;

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

        // The root is a stack pane
        root = new StackPane();
        root.setMaxWidth(window.getWidth());
        root.setMaxHeight(window.getHeight());

        // Root holds Border Pane, Border Pane holds everything else
        var menuPane = new BorderPane();
        root.getChildren().add(menuPane);

        //The whole left side that will hold the side menu and a button to hide it
        var leftSide = new HBox();
        leftSide.getStyleClass().add("campaign-list-bg");
        
        // Side menu on the left
        var sideMenu = new VBox();
        sideMenu.getStyleClass().add("sideBackground");
        sideMenu.setMinWidth(0);
        
        //Button next to the side menu to indicate sliding
        var slideButton = new Button("<");
        slideButton.getStyleClass().add("slideButton");
        
        //Compose the left side with the side menu and the slide button next to it
        leftSide.getChildren().addAll(sideMenu,slideButton);
		HBox.setMargin(slideButton, new Insets(sideMenu.getHeight()/2-26, 0, 0, 0)); //set slide btn in the mid
        
		//Update the slide button to be positioned in the middle of the side menu when it's resiszed
        sideMenu.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				HBox.setMargin(slideButton, new Insets(newValue.doubleValue()/2-26, 0, 0, 0));
			}
        	
        });
        
        // create an upload button on side menu
        var uploadButton = new Button("Upload a folder");
        uploadButton.setMinWidth(0);
        
        sideMenu.getChildren().addAll(uploadButton);
        
        //Functionality of the slide button
        slideButton.setOnMouseClicked((e) -> {
        	//Create timeline for animating the sidemenu
        	var timeline = new Timeline();
        	timeline.setCycleCount(1);
        	
        	if (sideMenu.getMaxWidth() != 0) {
        		//Save the calculated width of the side menu
        		sideMenuWidth = sideMenu.getWidth();
        		
        		//Set the max width of the side menu and upload button to the calculated widths on launch
        		sideMenu.setMaxWidth(sideMenuWidth);
        		uploadButton.setMaxWidth(uploadButton.getWidth());
	    		
        		//Animate max width of both properties to 0 to hide them
	    		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
	    				new KeyValue(sideMenu.maxWidthProperty(), 0, Interpolator.EASE_OUT),
	    				new KeyValue(uploadButton.maxWidthProperty(), 0, Interpolator.EASE_OUT)));
	    		timeline.play();
	    		
	    		//Change the text on the button to indicate that the menu can now be opened
	    		timeline.setOnFinished((ee) -> {
	    			slideButton.setText(">");
	    		});
        	} else {
        		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
	    				new KeyValue(sideMenu.maxWidthProperty(), sideMenuWidth, Interpolator.EASE_OUT),
	    				new KeyValue(uploadButton.maxWidthProperty(), sideMenuWidth, Interpolator.EASE_OUT)));
	    		timeline.play();
	    		
	    		timeline.setOnFinished((ee) -> {
	    			slideButton.setText("<");
	    		});
        	}
        });
        // click event of upload button
        uploadButton.setOnAction(event -> {
            window.openUploadPage();

        });

        // Scroll pane to hold the flow pane and enable scrolling
        var scrollPane = new ScrollPane();
        scrollPane.setContent(flowPane);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        
        // menu pane layout
        menuPane.setTop(title());
        menuPane.setLeft(leftSide);
        menuPane.setCenter(campaignList());
        BorderPane.setMargin(menuPane, new Insets(15, 0, 0, 15));
        
        var iter = sideMenu.getChildren().iterator();

        while (iter.hasNext()) {
            var currentButton = iter.next();
            VBox.setMargin(currentButton, new Insets(10, 10, 10, 10));
            currentButton.getStyleClass().add("buttonStyle");
        }
    }

    /*
     * The title on the page
     */
    private HBox title() {
        var mainMenuTextA = new Label("Ad Auction ");
        mainMenuTextA.getStyleClass().add("topTitle");
        mainMenuTextA.setTextFill(Color.WHITE);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        var mainMenuTextB = new Label("Dashboard");
        mainMenuTextB.getStyleClass().add("topTitle");
        mainMenuTextB.setTextFill(Color.valueOf("#004CBE"));

        var title = new HBox(region1, mainMenuTextA, mainMenuTextB, region2);
        title.getStyleClass().add("topBackground");

        return title;
    }

    /*
     * Returns a borderpane that holds a flowpane in the center
     * FlowPane holds all campaigns
     */
    private BorderPane campaignList() {
        var txt = new Label("Your Campaigns");
        txt.setMaxWidth(Double.MAX_VALUE);
        txt.setAlignment(Pos.CENTER);
        txt.setTextFill(Color.WHITE);

        flowPane = new FlowPane();
        flowPane.setPadding(new Insets(5, 0, 5, 0));
        flowPane.setMaxWidth(Double.MAX_VALUE);
        flowPane.setAlignment(Pos.TOP_CENTER);

        flowPane.setPrefWrapLength(500);
        updateAdvertiseList();

        var bp = new BorderPane();
        bp.setTop(txt);
        bp.setCenter(flowPane);
        bp.getStyleClass().add("campaign-list");

        return bp;
    }

    @SuppressWarnings("unchecked")
    private void updateAdvertiseList() {
        flowPane.getChildren().clear();

        List<CampaignData> campaigns = (List<CampaignData>) App.getInstance().controller()
                .query(ControllerQuery.GET_CAMPAIGNS).get();
        campaigns.forEach(c -> {
            var advertButton = makeAdvertButton(c.name());
            advertButton.setOnMouseClicked((e) -> {
            	window.openLoadPage(c.name());
            	
            	Task<Void> task = new Task<Void>() {
            		
            	    @Override 
            	    public Void call() {
            	    	App.getInstance().controller().query(ControllerQuery.OPEN_CAMPAIGN, c.name());
            	        return null;
            	    }
            	};
            	
            	task.setOnSucceeded((ee) -> {
            		window.openCampaignPage(c.name());
            	});            		
            	
            	new Thread(task).start();
            });
            
            flowPane.getChildren().add(advertButton);
        });

    }
    
    /**
     * Create an advert button with specific size and name
     * @param name - the text on the button
     */
    private Button makeAdvertButton(String name) {
    	var advertButton = new Button(name);
    	advertButton.getStyleClass().add("advertButton");
    	
    	advertButton.setMinWidth(200);
    	advertButton.setMinHeight(100);
    	advertButton.setMaxWidth(200);
    	advertButton.setMaxHeight(100);
    	
    	return advertButton;
    }

}
