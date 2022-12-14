package ad.auction.dashboard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.view.ui.Window;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Ad Auction Dashboard application wrapper
 *
 * @author group 7
 */
public class App extends Application {
    private static final Logger logger = LogManager.getLogger(App.class.getSimpleName());

    //Will be changed
    private static final int HEIGHT = 600;
    private static final int WIDTH = 1200;

    // the singleton instance variable of app
    private static App instance;

    private final Controller controller = new Controller();
    private Window window;

    @Override
    public void start(Stage stage) {
    	instance = this;
    	
        this.window = new Window(stage,HEIGHT,WIDTH);
        stage.show();
    }

    /**
     * Shuts down the application
     */
    public void shutdown() {
        logger.info("Closing application");
        this.controller.close();
    	
    	System.exit(0);
    }
    
    /**
     * Return the application instance
     * @return the application instance
     */
    public static App getInstance() { return instance; }

    /**
     * @return The controller to communicate with the model
     */
    public Controller controller() {
        return this.controller;
    }

    /**
     * @return Currently displayed window
     */
    public Window window() {
        return this.window;
    }
}