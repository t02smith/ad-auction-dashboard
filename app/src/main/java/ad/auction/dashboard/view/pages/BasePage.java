package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.view.ui.Window;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * The base page that holds common elements among all pages
 * @author hhg1u20
 *
 */
public abstract class BasePage {

	protected final Window window;

    protected Scene scene;
    protected StackPane root = new StackPane();

    public BasePage(Window window) {
        this.window = window;
    }

    /**
     * Build the layout of the page
     */
    public abstract void build();
    
    /**
     * Create a new JavaFX scene
     * @return JavaFX scene
     */
    public Scene createScene() {
        var previous = window.getScene();
        Scene scene = new Scene(root, previous.getWidth(), previous.getHeight(), Color.BLACK);
        scene.getStylesheets().add(getClass().getResource("/style/darkandblue.css").toExternalForm());
        this.scene = scene;
        
        return scene;
    }

}
