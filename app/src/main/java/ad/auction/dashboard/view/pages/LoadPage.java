package ad.auction.dashboard.view.pages;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.view.ui.Window;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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

	public LoadPage(Window window) {
		super(window);
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
        updateScaling();
        
	    root.getChildren().addAll(mainPane);
	    mainPane.getStyleClass().add("campaign-list");

	    mainPane.add(threeDotsAnim(), 1, 1);
	    
	    window.addScaleListener(() -> {
	    	updateScaling();
	    });
	}

	/*
	 * The three dots loading in the middle
	 */
	private HBox threeDotsAnim() {
		var hbox = new HBox();

		
		for (int i = 0; i < 30; i+=10) {
			//Circles to represent dots
			var dotCircle = new Circle(0, i, 10);
			hbox.getChildren().add(dotCircle);
			HBox.setMargin(dotCircle, new Insets(0, 7, 0, 0));
			
			//The fade animation for each circle
			var timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);
	        timeline.setAutoReverse(true);
			dotCircle.setFill(Color.WHITE);
			
			timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1400),
	        		new KeyValue(dotCircle.opacityProperty(), 0, Interpolator.EASE_BOTH)));
			
			timeline.play();
			
			try { TimeUnit.MILLISECONDS.sleep(400);	} 
			catch (InterruptedException e) { e.printStackTrace(); }
		}
		
		return hbox;
	}
	/**
	 * Update scaling of the gridpane according to the window
	 */
	private void updateScaling() {
		var scaleFactorW = 2.3;
		var scaleFactorH = 2.6;
		
		if (window.getWidth() > 600) scaleFactorW = 2.3 - (window.getWidth()/100)/100;
		if (window.getHeight() > 600) scaleFactorH = 2.6 - (window.getHeight()/50)/100;
		
		mainPane.setHgap(window.getWidth()/scaleFactorW);
	    mainPane.setVgap(window.getHeight()/scaleFactorH);
	}
}
