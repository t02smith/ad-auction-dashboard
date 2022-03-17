package ad.auction.dashboard.view.pages;

import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.ui.Window;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Main Menu page that holds the UI elements for its controls
 *
 * @author tcs1g20, hhg1u20
 */
public class MenuPage extends BasePage {

    private static final Logger logger = LogManager.getLogger(MenuPage.class.getSimpleName());
    private FlowPane flowPane; // the flow pane contains the campaign buttons

    public MenuPage(Window window) {
        super(window);
        logger.info("Creating Menu Page");

    }

    /**
     * Builds the UI elements on the page
     */
    @Override
    public void build() {
        logger.info("Building Main Menu");

        // The root is a stack pane
        root = new StackPane();
        root.setMaxWidth(window.getWidth());
        root.setMaxHeight(window.getHeight());

        // Root holds Border Pane, Border Pane holds everything else
        var menuPane = new BorderPane();
        root.getChildren().add(menuPane);

        // Side menu on the left
        var sideMenu = new VBox();
        sideMenu.getStyleClass().add("sideBackground");

        // create an upload button on side menu
        var uploadButton = new Button("Upload a folder");
        sideMenu.getChildren().addAll(uploadButton);
        
        // click event of upload button
        uploadButton.setOnAction(event -> {
            window.openUploadPage();

        });

        // Scroll pane to hold the flow pane and enable scrolling
        var scrollPane = new ScrollPane();
        scrollPane.setContent(flowPane);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        
        // menu pane layout
        menuPane.setTop(title());
        menuPane.setLeft(sideMenu);
        menuPane.setCenter(campaignList());
        BorderPane.setMargin(menuPane, new Insets(15, 0, 0, 15));
        
        var iter = sideMenu.getChildren().iterator();

        while (iter.hasNext()) {
            var currentButton = iter.next();
            VBox.setMargin(currentButton, new Insets(10, 10, 10, 10));
            currentButton.getStyleClass().add("buttonStyle");
        }
    }

    /*
     * The title on the page
     */
    private HBox title() {
        var mainMenuTextA = new Label("Ad Auction ");
        mainMenuTextA.getStyleClass().add("topTitle");
        mainMenuTextA.setTextFill(Color.WHITE);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        var mainMenuTextB = new Label("Dashboard");
        mainMenuTextB.getStyleClass().add("topTitle");
        mainMenuTextB.setTextFill(Color.valueOf("#004CBE"));

        var title = new HBox(region1, mainMenuTextA, mainMenuTextB, region2);
        title.getStyleClass().add("topBackground");

        return title;
    }

    /*
     * Returns a borderpane that holds a flowpane in the center
     * FlowPane holds all campaigns
     */
    private BorderPane campaignList() {
        var txt = new Label("Your Campaigns");
        txt.setMaxWidth(Double.MAX_VALUE);
        txt.setAlignment(Pos.CENTER);
        txt.setTextFill(Color.WHITE);

        flowPane = new FlowPane();
        flowPane.setPadding(new Insets(5, 0, 5, 0));
        flowPane.setMaxWidth(Double.MAX_VALUE);
        flowPane.setAlignment(Pos.TOP_CENTER);

        flowPane.setPrefWrapLength(500);
        updateAdvertiseList();

        var bp = new BorderPane();
        bp.setTop(txt);
        bp.setCenter(flowPane);
        bp.getStyleClass().add("campaign-list");

        return bp;
    }

    private void updateAdvertiseList() {
        flowPane.getChildren().clear();

        var campaigns = App.getInstance().controller().getCampaigns();

        campaigns.forEach(c -> {
            logger.info(c.name());
            var advertButton = new Button(c.name());
            advertButton.getStyleClass().add("advertButton");
            advertButton.setOnMouseClicked((e) -> {
                window.openLoadPage(c.name());
            	
            	Task<Void> task = new Task<Void>() {
            		
            	    @Override 
            	    public Void call() {
            	    	Future<Void> res = App.getInstance().controller().openCampaign(c.name());
                        while (!res.isDone()) {}
            	        return null;
            	    }
            	};
            	
            	task.setOnSucceeded((ee) -> {
            		window.openCampaignPage(c.name());
            	});  

                new Thread(task).start();
            });
            flowPane.getChildren().add(advertButton);
        });
    }

}
