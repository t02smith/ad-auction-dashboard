package ad.auction.dashboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.view.ui.Window;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
	// stage of the app
	private Stage stage;
    private static final Logger logger = LogManager.getLogger(App.class.getSimpleName());



    //Will be changed
    private static final int HEIGHT = 600;
    private static final int WIDTH = 1200;

    // the singleton instance variable of app
    private static App instance;

    private final Controller controller = new Controller();


    @Override
    public void start(Stage stage) {
    	instance = this;
    	
        this.stage = stage;
        openWindow();
    }
    
    /**
     * Opens the window of the application
     */
    private void openWindow() {
    	var window = new Window(stage,HEIGHT,WIDTH);
    	stage.show();
    }
    
    /**
     * Shuts down the application
     */
    public void shutdown() {
    	logger.info("Closing application");
    	System.exit(0);
    }

    public Controller controller() {
        return this.controller;
    }
    
    /**
     * Return the application instance
     * @return the application instance
     */
    public static App getInstance() { return instance; }


}