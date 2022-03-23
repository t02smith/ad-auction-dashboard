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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
    private String campaignName;

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
	    
	    //BorderPane to hold a vbox in the center with the animation and the label
	    var mainPane = new BorderPane();
	    var vbox = new VBox();
	    
	    mainPane.getStyleClass().add("load-page");
	    
	    //Initialise the animation and the label
		HBox threeDots = threeDotsAnim();
		Label campaignNameLabel = new Label(campaignName);
	    campaignNameLabel.setTextFill(Color.WHITE);
	    
	    //Attach nodes to each other
	    mainPane.setCenter(vbox);
	    root.getChildren().addAll(mainPane);
	    vbox.getChildren().addAll(threeDots, campaignNameLabel);

	    //Position everything in the center
	    threeDots.setAlignment(Pos.CENTER);
	    campaignNameLabel.setAlignment(Pos.CENTER);
	    vbox.setAlignment(Pos.CENTER);
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
    	task.exceptionProperty().addListener((observable, oldValue, newValue) -> newValue.printStackTrace());
    	
		return hbox;
	}
}
