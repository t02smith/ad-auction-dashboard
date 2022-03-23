package ad.auction.dashboard.view.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.pages.*;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The window of the application
 * @auth hhg1u20
 */
public class Window {

    private static final Logger logger = LogManager.getLogger(Window.class.getSimpleName());

	private final int height;
	private final int width;

	private final Stage stage;
	private Scene scene;
	
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
	}

	/**
	 * Setup the basics of the stage - title, size and behaviour
	 */
	private void setupStage() {
		logger.info("Setting up the stage");
		
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
		logger.info("Setting up default scaling");
		
		this.scene = new Scene(new Pane(), width, height, Color.BLUE);
		
		stage.setScene(this.scene);
		stage.setMinWidth(300);
		stage.setMinHeight(300);
	}

	/**
	 * Loads the main menu
	 */
	public void startMenu() {
		loadPage(new MainMenuPage(this));
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

	public void openLoadPage(String name) {
		loadPage(new LoadPage(this,name));
	}

	public void openEditPage(String campaign) {
		loadPage(new EditPage(this, campaign));
	}

	/**
	 * Load a given page
	 * 
	 * @param newPage - the new page to be loaded
	 */
	private void loadPage(BasePage newPage) {
		// build the page ui
		newPage.build();
		// create scene
		scene = newPage.createScene();
		// show scene of the new page
		stage.setScene(scene);
	}

	public Scene getScene() {
		return this.scene;
	}

	public double getWidth() {
		return stage.getWidth();
	}

	public double getHeight() {
		return stage.getHeight();
	}
}
