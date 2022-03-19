package ad.auction.dashboard.view.ui;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.pages.*;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
	 * 
	 *              TODO: Func take actual graph to display
	 */
	public void openCampaignPage(String campaign) {
		loadPage(new CampaignPage(this, campaign));
	}


	public void openUploadPage() {
		loadPage(new UploadPage(this));
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

}
