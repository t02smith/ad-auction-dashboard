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

public class LoadPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(LoadPage.class.getSimpleName());

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
	    var mainPane = new GridPane();
        mainPane.setHgap(window.getWidth()/1.7);
        mainPane.setVgap(window.getHeight()/1.7);
        
	    root.getChildren().add(mainPane);
	    mainPane.getStyleClass().add("campaign-list");

	    mainPane.add(threeDotsAnim(), 1, 1);
	}

	/*
	 * The three dots loading in the middle
	 */
	private HBox threeDotsAnim() {
		var hbox = new HBox();

		
		for (int i = 0; i < 30; i+=10) {
			var dotCircle = new Circle(0, i, 10);
			hbox.getChildren().add(dotCircle);
			HBox.setMargin(dotCircle, new Insets(0, 7, 0, 0));
			
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
}
