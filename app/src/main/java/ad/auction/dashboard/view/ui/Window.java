package ad.auction.dashboard.view.ui;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.pages.AdvertPage;
import ad.auction.dashboard.view.pages.BasePage;
import ad.auction.dashboard.view.pages.MenuPage;
import javafx.scene.Scene;
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
	
	public Window(Stage stage,int height, int width) {
		this.stage = stage;
		
		this.height = height;
		this.width = width;
		
		setupStage();
		setupDefaultWindow();
		
		startMenu();
	}
	
	/**
	 * Setup the basics of the stage - title, size and behaviour
	 */
	private void setupStage() {
		stage.setTitle("Ad Auction Dashboard");
		stage.setMinWidth(width);
		stage.setMinHeight(height);
        stage.setOnCloseRequest(ev -> App.getInstance().shutdown());
	}
	
	/**
	 * Default scene
	 */
	private void setupDefaultWindow() {
		this.scene = new Scene(new Pane(),width,height, Color.BLUE);
		stage.setScene(this.scene);
	}
	
	/**
	 * Loads the main menu
	 */
	public void startMenu() { loadPage(new MenuPage(this)); }
	
	/**
	 * Loads an advertisement page with a given graph
	 * @param graph - graph to be plotted
	 * 
	 * TODO: Func take actual graph to display
	 */
	public void openAdvertPage(int graph) { loadPage(new AdvertPage(this, graph)); }
	
	/**
	 * Updates the advertisement page graph according to the desired plotting
	 */
	public void updateAdvertPage(int category) { currentPage.update(category); }
	
	/**
	 * Load a given page
	 * @param newPage - the new page to be loaded
	 */
	private void loadPage(BasePage newPage) {
		newPage.build();
        currentPage = newPage;
        scene = newPage.createScene();
        stage.setScene(scene);
	}
	
	public Scene getScene() { return this.scene; }
	
	public double getWidth() { return this.width; }
	
	public double getHeight() { return this.height; }
	
}
