package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The upload file page view class
 */
public class UploadPage extends BasePage {
    private static final Logger logger = LogManager.getLogger(UploadPage.class.getSimpleName());

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
        
        // submit and cancel button
        Button okButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");
        FlowPane flowPane = new FlowPane(okButton, cancelButton);
        flowPane.setHgap(20);
        okButton.setOnAction(event -> {
            String campaignsName = campaignsNameText.getText();
            String impressionsFilePath = filePaths.get(1);
            String clicksFilePath = filePaths.get(0);
            String serverFilePath = filePaths.get(2);
            // check if the input is ok
            if (!campaignsName.isEmpty() && !impressionsFilePath.isEmpty() && !clicksFilePath.isEmpty() && !serverFilePath.isEmpty()) {

                try {
                    boolean[] output = App.getInstance().controller().newCampaign(campaignsName,
                        clicksFilePath, impressionsFilePath, serverFilePath).get();

                    if (!(output[0] && output[1] && output[2])) {
                        // alert message
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Hint");
                        alert.setHeaderText("Warning");
                        alert.setContentText("Incorrect file formats submitted");
                        alert.showAndWait();
                    }
                } catch (Exception err) {}

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
