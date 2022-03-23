package ad.auction.dashboard.view.pages;

import java.io.InputStream;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.ui.Window;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
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
    private double titlePaneHeight;
    private boolean hoveredTitle = false;
    private boolean pinSelected = true;

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
        leftSide.setAlignment(Pos.CENTER);
        
        // create an upload button on side menu
        var uploadButton = new Button("Create Campaign");
        uploadButton.setMinWidth(0);
        
        sideMenu.getChildren().addAll(uploadButton);
        
        //Functionality of the slide button
        slideButton.setOnMouseClicked((e) -> {
        	slideButton.setDisable(true);
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
        		timeline.stop();
        		timeline.getKeyFrames().clear();

	    		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
	    				new KeyValue(sideMenu.maxWidthProperty(), 0, Interpolator.EASE_OUT),
	    				new KeyValue(uploadButton.maxWidthProperty(), 0, Interpolator.EASE_OUT)));
	    		timeline.play();
	    		
	    		//Change the text on the button to indicate that the menu can now be opened
	    		timeline.setOnFinished((ee) -> {
	    			slideButton.setDisable(false);
	    			slideButton.setText(">");
	    		});
        	} else {
        		timeline.stop();
        		timeline.getKeyFrames().clear();

        		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
	    				new KeyValue(sideMenu.maxWidthProperty(), sideMenuWidth, Interpolator.EASE_OUT),
	    				new KeyValue(uploadButton.maxWidthProperty(), sideMenuWidth, Interpolator.EASE_OUT)));
	    		timeline.play();
	    		
	    		timeline.setOnFinished((ee) -> {
	    			slideButton.setDisable(false);
	    			slideButton.setText("<");
	    		});
        	}
        });
        
        // click event of upload button
        uploadButton.setOnAction(event -> window.openUploadPage());

        // Scroll pane to hold the flow pane and enable scrolling
        var scrollPane = new ScrollPane();
        scrollPane.setContent(flowPane);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        
        scrollPane.setMinWidth(0);
        scrollPane.setMinHeight(0);
        
        // menu pane layout
        menuPane.setTop(title());
        menuPane.setLeft(leftSide);
        menuPane.setCenter(campaignList());
        BorderPane.setMargin(menuPane, new Insets(15, 0, 0, 15));

        for (javafx.scene.Node currentButton : sideMenu.getChildren()) {
            VBox.setMargin(currentButton, new Insets(10, 10, 10, 10));
            currentButton.getStyleClass().add("buttonStyle");
        }
    }

    /*
     * The title on the page
     */
    private BorderPane title() {
    	var titlePane = new BorderPane();
    	titlePane.setMinHeight(0);
    	titlePane.getStyleClass().add("topTitle");
    	
        var mainMenuTextA = new Label("Ad Auction ");
        mainMenuTextA.getStyleClass().add("topTitle");
        mainMenuTextA.setTextFill(Color.WHITE);

        var mainMenuTextB = new Label("Dashboard");
        mainMenuTextB.getStyleClass().add("topTitle");
        mainMenuTextB.setTextFill(Color.valueOf("#004CBE"));

        var title = new HBox(mainMenuTextA, mainMenuTextB);
        title.setAlignment(Pos.CENTER);
        title.getStyleClass().add("topBackground");

		InputStream pinSelectedStream = this.getClass().getResourceAsStream("/img/pinSelected.png");
		InputStream pinUnselectedStream = this.getClass().getResourceAsStream("/img/pinUnselected.png");

		ImageView pinSelectedIcon = new ImageView();
		ImageView pinUnselectedIcon = new ImageView();
		
		pinSelectedIcon.setFitWidth(33);
		pinSelectedIcon.setFitHeight(47);
		pinUnselectedIcon.setFitWidth(33);
		pinUnselectedIcon.setFitHeight(47);
		

		var pinButton = new Button();
		pinButton.setGraphic(pinSelectedIcon);
		pinButton.getStyleClass().add("titleImageButton");
		
		pinButton.setOnAction((e) -> {
			if (pinSelected) {
				pinSelected = false;
				pinButton.setGraphic(pinUnselectedIcon);
			} else {
				pinSelected = true;
				pinButton.setGraphic(pinSelectedIcon);
			}
		});
		
		pinSelectedIcon.setImage(new Image(pinSelectedStream));
		pinUnselectedIcon.setImage(new Image(pinUnselectedStream));
		
        var timeline = new Timeline();
    	timeline.setCycleCount(0);
    	
        titlePane.setOnMouseEntered((e) -> {
        	
        	if (!hoveredTitle) {
        		hoveredTitle = true;
        		titlePaneHeight = titlePane.getHeight();
        		titlePane.setMaxHeight(titlePaneHeight);
        	}
        	
        	if (!pinSelected) {
        		
        		logger.info("PLAYING ANIMATION " + titlePaneHeight);
        		
        		timeline.stop();
        		timeline.getKeyFrames().clear();
        		
        		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
	    				new KeyValue(titlePane.maxHeightProperty(), 100, Interpolator.EASE_OUT)));
	    		timeline.play();
	    		
	    		timeline.setOnFinished((ee) -> {
	    			logger.info("STOPPED AFTER ENTER " + titlePane.getMaxHeight());
//	    			titlePane.setMaxHeight(titlePaneHeight);
	    		});
        	}
        });
        
        titlePane.setOnMouseExited((e) -> {
        	if (!pinSelected) {
        		logger.info("STOP THE ANIMATION");
        		
        		timeline.stop();
        		timeline.getKeyFrames().clear();
        		
        		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(500),
	    				new KeyValue(titlePane.maxHeightProperty(), 10, Interpolator.EASE_OUT)));
	    		timeline.play();
	    		
	    		timeline.setOnFinished((ee) -> {
	    			logger.info("STOPPED AFTER EXIT");
//	    			titlePane.setMaxHeight(10);
	    		});
        	}
        });
        
        titlePane.setRight(pinButton);
        titlePane.setCenter(title);
        
        return titlePane;
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

    private void updateAdvertiseList() {
        flowPane.getChildren().clear();

        var campaigns = App.getInstance().controller().getCampaigns();

        campaigns.forEach(c -> {
            logger.info(c.name());
            var advertButton = new Button(c.name());
            advertButton.getStyleClass().add("advertButton");
            advertButton.setOnMouseClicked((e) -> {
                window.openLoadPage(c.name());
            	
            	Task<Void> task = new Task<Void>() {
            		
            	    @Override 
            	    public Void call() {
            	    	Future<Void> res = App.getInstance().controller().openCampaign(c.name());
                        while (!res.isDone()) {}
            	        return null;
            	    }
            	};
            	
            	task.setOnSucceeded((ee) -> window.openCampaignPage(c.name()));

                new Thread(task).start();
            });
            flowPane.getChildren().add(advertButton);
        });
    }
}
