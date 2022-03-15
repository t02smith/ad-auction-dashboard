package ad.auction.dashboard.view.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.controller.Controller.ControllerQuery;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * The upload file page view class
 */
public class UploadPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(UploadPage.class.getSimpleName());

    private final Controller controller = App.getInstance().controller();

    private GridPane gridPane;
    
    public UploadPage(Window window) {
        super(window);
    }

    @Override
    public void build() {
        logger.info("Building Main Menu");

        //The root is a stack pane
        root = new StackPane();
        root.setMaxWidth(window.getWidth());
        root.setMaxHeight(window.getHeight());

        //Root holds Border Pane, Border Pane holds everything else
        var borderPane = new BorderPane();
        root.getChildren().add(borderPane);


        //Title background on top
        var title = new BorderPane();
        title.getStyleClass().add("topBackground");

        //Title text on top
        var mainMenuText = new Text("Upload file");
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);

        // create a grid pane layout the controls
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(100, 0, 0, 350));

        // campaign name input
        Label campaignsLabel = new Label("Campaign name:");
        TextField campaignsNameText = new TextField("");
        gridPane.add(campaignsLabel, 0, 0);
        gridPane.add(campaignsNameText, 1, 0);


        ArrayList<String> filePaths = createFieldAll("Clicks file","Impressions file","Server file");
        // impression file selection
//        Label impressionsLabel = new Label("Impression file:");
//        TextField impressionsText = new TextField("");
//        Button impressionsButton = new Button("Select file");
//        gridPane.add(impressionsLabel, 0, 1);
//        gridPane.add(impressionsText, 1, 1);
//        gridPane.add(impressionsButton, 2, 1);
//        impressionsButton.setOnAction(event -> {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.csv", List.of("*.csv")));
//            File file = fileChooser.showOpenDialog(null);
//            if (file != null) {
//                impressionsText.setText(file.getAbsolutePath());
//            }
//        });

        // clicks file selection
//        Label clicksLabel = new Label("Clicks file:");
//        TextField clicksText = new TextField("");
//        Button clicksButton = new Button("Select file");
//        gridPane.add(clicksLabel, 0, 2);
//        gridPane.add(clicksText, 1, 2);
//        gridPane.add(clicksButton, 2, 2);
//        clicksButton.setOnAction(event -> {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.csv", List.of("*.csv")));
//            File file = fileChooser.showOpenDialog(null);
//            if (file != null) {
//                clicksText.setText(file.getAbsolutePath());
//            }
//        });

        // server file selection
//        Label serverLabel = new Label("Server file:");
//        TextField serverText = new TextField("");
//        Button serverButton = new Button("Select file");
//        gridPane.add(serverLabel, 0, 3);
//        gridPane.add(serverText, 1, 3);
//        gridPane.add(serverButton, 2, 3);
//        serverButton.setOnAction(event -> {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.csv", List.of("*.csv")));
//            File file = fileChooser.showOpenDialog(null);
//            if (file != null) {
//                serverText.setText(file.getAbsolutePath());
//            }
//        });

        // submit and cancel button
        Button okButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");
        
        FlowPane flowPane = new FlowPane(okButton, cancelButton);
        flowPane.setHgap(20);
        
        okButton.setOnAction(event -> {
            String campaignsName = campaignsNameText.getText();

            // Ask controller to make a new campaign
            if (filePaths.size() == 3) {
                String clicksFilePath = filePaths.get(0);
                String impressionsFilePath = filePaths.get(1);
                String serverFilePath = filePaths.get(2);
                
                controller.query(ControllerQuery.NEW_CAMPAIGN, 
                		campaignsName, clicksFilePath, impressionsFilePath, serverFilePath);
                
                window.startMenu();
            } else {
                // alert message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hint");
                alert.setHeaderText("Warning");
                alert.setContentText("Please fill all text fields before submitting!");
                alert.showAndWait();
            }

            
        });
        cancelButton.setOnAction(event -> {
            window.startMenu();
        });
        gridPane.add(flowPane, 1, 4);
        borderPane.setTop(title);
        borderPane.setCenter(gridPane);
    }

    /*
     * Create fields to import files
     */
    private ArrayList<String> createFieldAll(String...fields) {
    	logger.info("Creating Input Fields");
    	
    	var filePaths = new ArrayList<String>(); //each filepath selected
        var buttonIndex = new HashMap<Button, Integer>(); //map each button to an index
        
    	int insetRow = 1;
    	int index = 0;
    	
    	for (String field:fields) {
            Button selectButton = new Button("Select " + field + "..");
            var ind = index + 1;
            
            gridPane.add(selectButton, 1, insetRow);
            insetRow++;
            
            //Add functionality to the select button
            selectButton.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.csv", List.of("*.csv")));
                File file = fileChooser.showOpenDialog(null);
                
                if (file != null) { 
                	if (buttonIndex.get(selectButton) == null) { 
                        buttonIndex.put(selectButton, ind);                        
                		filePaths.add(file.getAbsolutePath()); 
                	} else filePaths.set(buttonIndex.get(selectButton), file.getAbsolutePath());
                }
            });
            
            index++;
    	}
    	
    	return filePaths;
    }
}
