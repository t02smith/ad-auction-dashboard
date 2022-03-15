package ad.auction.dashboard.view.ui;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.pages.BasePage;
import ad.auction.dashboard.view.pages.CampaignPage;
import ad.auction.dashboard.view.pages.LoadPage;
import ad.auction.dashboard.view.pages.MenuPage;
import ad.auction.dashboard.view.pages.UploadPage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/*
 * The window of the application
 * @auth hhg1u20
 */
public class Window {

	private final int height;
	private final int width;

	private final Stage stage;
	private Scene scene;
	private BasePage currentPage;
	
	protected ScaleListener scaleListener;

	// initialize with stage height and width
	public Window(Stage stage, int height, int width) {
		this.stage = stage;

		this.height = height;
		this.width = width;

		// setup stage
		setupStage();
		// setup an empty pane as default scene
		setupDefaultWindow();
		// show menu page as the first page
		startMenu();
		//openLoadPage();
	}

	/**
	 * Setup the basics of the stage - title, size and behaviour
	 */
	private void setupStage() {
		stage.setTitle("Ad Auction Dashboard");
		stage.getIcons().add(new Image(this.getClass().getResource("/img/logo.png").toExternalForm()));
		stage.setMinWidth(width);
		stage.setMinHeight(height);
		stage.setOnCloseRequest(ev -> App.getInstance().shutdown());
	}

	/**
	 * Default scene
	 */
	private void setupDefaultWindow() {
		this.scene = new Scene(new Pane(), width, height, Color.BLUE);
		stage.setScene(this.scene);
		
		scaleListener = new ScaleListener(scene);
		
		stage.widthProperty().addListener(scaleListener);
		stage.heightProperty().addListener(scaleListener);
	}

	/**
	 * Loads the main menu
	 */
	public void startMenu() {
		loadPage(new MenuPage(this));
	}

	/**
	 * Loads an advertisement page with a given graph
	 * 
	 * @param graph - graph to be plotted
	 */
	public void openCampaignPage(String campaign) {
		loadPage(new CampaignPage(this, campaign));
	}


	public void openUploadPage() {
		loadPage(new UploadPage(this));
	}

	public void openLoadPage() {
		loadPage(new LoadPage(this));
	}
	/**
	 * Load a given page
	 * 
	 * @param newPage - the new page to be loaded
	 */
	private void loadPage(BasePage newPage) {
		// build the page ui
		newPage.build();
		// set current page
		currentPage = newPage;
		// create scene
		scene = newPage.createScene();
		// show scene of the new page
		stage.setScene(scene);
	}

	public Scene getScene() {
		return this.scene;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	/**
	 * Listener bound to the scaling of the scene
	 * @author hhg1u20
	 *
	 */
	public class ScaleListener implements ChangeListener<Number>{

		private Scene scene;
		private double width;
		private double height;
		private double scalar;
		
		//Get the scene
		public ScaleListener(Scene sceneP) {
			this.scene = sceneP;
			this.width = scene.getWidth();
			this.height = scene.getHeight();
		}
		
		//The method invoked automatically on change
		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			//The new size of the window
			double newWidth = scene.getWidth();
			double newHeight = scene.getHeight();
			
			//Update the scalar for resizing nodes depending on the aspect ratio
			if (newWidth/newHeight > width/height) setScalar(newHeight/height);
			else setScalar(newWidth/width);
			
			if (scalar < 1) {
				scene.getRoot().prefHeight(Math.max(height, newHeight));
				scene.getRoot().prefWidth(Math.max(width, newWidth));
			} else {
				Scale scale = new Scale(scalar, scalar);
		        scale.setPivotX(0);
		        scale.setPivotY(0);
		        scene.getRoot().getTransforms().setAll(scale);

		        scene.getRoot().prefWidth (newWidth / scalar);
		        scene.getRoot().prefHeight(newHeight / scalar);
			}
		}
		
		//The scalar to multiply scaling with
		private void setScalar(double scalar) { this.scalar = scalar; }
	}
}
