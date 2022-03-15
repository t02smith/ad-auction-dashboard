package ad.auction.dashboard.view.pages;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.view.ui.Window;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Loading page to be presented when something is loading up
 * @author hhg1u20
 *
 */
public class LoadPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(LoadPage.class.getSimpleName());
    private GridPane mainPane;
    private String campaignName;
    private Label campaignNameLabel;
    private HBox threeDots;

	public LoadPage(Window window) {
		super(window);
	}
	
	public LoadPage(Window window, String campaignName) {
		super(window);
		this.campaignName = "Loading " + campaignName;
	}

	@Override
	public void build() {
		logger.info("Building load page");
		
		//definte the root
		root = new StackPane();
	    root.setMaxWidth(window.getWidth());
	    root.setMaxHeight(window.getHeight());
	    
	    //BorderPane to hold the things on the center
	    mainPane = new GridPane();
        
	    root.getChildren().addAll(mainPane);
	    mainPane.getStyleClass().add("campaign-list");

	    threeDots = threeDotsAnim();
	    var vbox = new VBox();
	    vbox.getChildren().add(threeDots);
	    mainPane.add(vbox, 1, 1);
	    
	    campaignNameLabel = new Label(campaignName);
	    campaignNameLabel.setTextFill(Color.WHITE);
	    
	    vbox.getChildren().add(campaignNameLabel);
	    updateScaling();
	    
	    window.addScaleListener(() -> {
	    	updateScaling();
	    });
	}

	/*
	 * The three dots loading in the middle
	 */
	private HBox threeDotsAnim() {
		var hbox = new HBox();
		var listCircles = new ArrayList<Circle>();
		
		//Set up the three circles
		for (int i = 0; i < 3; i++) {
			var dotCircle = new Circle(0, i, 10);
    		hbox.getChildren().add(dotCircle);
    		HBox.setMargin(dotCircle, new Insets(0, 7, 0, 0));
    		dotCircle.setFill(Color.WHITE);
    		
    		listCircles.add(dotCircle);
		}
		
		//Animate the circles on a separate thread
		Task<Void> task = new Task<Void>() {
			
    	    @Override 
    	    public Void call() {
    	    	for (int i = 0; i < 3; i++) {
    	    		//The fade animation for each circle
    	    		var timeline = new Timeline();
    	    		timeline.setCycleCount(Timeline.INDEFINITE);
    	    		timeline.setAutoReverse(true);
    	    		
    	    		timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1400),
    	    				new KeyValue(listCircles.get(i).opacityProperty(), 0, Interpolator.EASE_BOTH)));
    	    		
    	    		timeline.play();
    	    		
    	    		try { Thread.sleep(300);	} 
    	    		catch (InterruptedException e) { e.printStackTrace(); }
    	    		
    	    		if (isCancelled()) break;
    	    	}
    	    	return null;
    	    }
    	};

    	new Thread(task).start();
    	
    	//Print exception if something goes wrong in the animation thread
    	task.exceptionProperty().addListener((observable, oldValue, newValue) ->  { newValue.printStackTrace();	});
    	
		return hbox;
	}
	/*
	 * Update position of the dots and the label according to the window and label size
	 */
	private void updateScaling() {
		int letterInPixels = 5;
		if (campaignNameLabel.getText().length() < 15) letterInPixels = 3;

		var campNameOffset = window.getWidth()/2-40-campaignNameLabel.getText().length()*letterInPixels;
		VBox.setMargin(threeDots, new Insets(0, 0, 0, window.getWidth()/2-40));
		VBox.setMargin(campaignNameLabel, new Insets(0, 0, 0, campNameOffset));
		
		var scaleFactorH = 2.6;
		
		if (window.getHeight() > 600) scaleFactorH = 2.6 - (window.getHeight()/50)/100;
		else scaleFactorH = 2.6;
		
		mainPane.setVgap(window.getHeight()/scaleFactorH);
	}
}
