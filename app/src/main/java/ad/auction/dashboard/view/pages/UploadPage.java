package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import ad.auction.dashboard.controller.Controller;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * The upload file page view class
 */
public class UploadPage extends BasePage {
	
    private static final Logger logger = LogManager.getLogger(UploadPage.class.getSimpleName());
    private final Controller controller = App.getInstance().controller();
    
    private String workFolderPath = null;

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
        var mainPane = new BorderPane();
        root.getChildren().add(mainPane);

        //Title background on top
        var title = new BorderPane();
        title.getStyleClass().add("topBackground");
        
        //Title text on top
        var mainMenuText = new Text("Upload File");
        mainMenuText.getStyleClass().add("topTitle");
        title.setCenter(mainMenuText);

        // use vbox as the root layout
        VBox mainVBox = new VBox();
        mainVBox.setAlignment(Pos.CENTER);

        //Test
        TextField campaignNameText = new TextField();
        campaignNameText.setPromptText("Campaign name");
        campaignNameText.setFocusTraversable(false);
        
        //Layout of the content
        var uploadFilesBox = new HBox();
        var clicksBox = new csvUploadBox("Clicks");
        var impressionsBox = new csvUploadBox("Impressions");
        var serverBox = new csvUploadBox("Server");
        var submitCancelBox = new HBox();
        
        //Spacing between nodes
        uploadFilesBox.setSpacing(20);
        submitCancelBox.setSpacing(40);
        
        VBox.setMargin(campaignNameText, new Insets(0, 0, 20, 0));
        VBox.setMargin(submitCancelBox, new Insets(20, 0, 0, 0));
        
        //Position children in the center of their panes
        uploadFilesBox.setAlignment(Pos.CENTER);
        submitCancelBox.setAlignment(Pos.CENTER);
        
        //Fix width of campaign name textfield
        campaignNameText.setMaxWidth(200);
        
        //Submit and cancel button
        Button okButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");
        
        okButton.getStyleClass().add("buttonStyle");
        cancelButton.getStyleClass().add("buttonStyle");
        
        okButton.setOnAction(event -> {
        	
            // check if the input is ok
            if (!campaignNameText.getText().isEmpty() && !impressionsBox.filePath.isEmpty() 
            		&& !clicksBox.filePath.isEmpty() && !serverBox.filePath.isEmpty()) {
                controller.newCampaign(campaignNameText.getText(), 
                		clicksBox.filePath, impressionsBox.filePath, serverBox.filePath);
                
                window.startMenu();
            } else { // If the input is faulty, display Alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Hint");
                alert.setHeaderText("Warning");
                alert.setContentText("Please fill all text field before submit!");
                alert.showAndWait();
            }
        });
        
        cancelButton.setOnAction(event -> { window.startMenu(); });

        mainPane.setTop(title);
        mainPane.setCenter(mainVBox);
        mainPane.getStyleClass().add("upload-list");

        mainVBox.getChildren().addAll(campaignNameText, uploadFilesBox, submitCancelBox);
        uploadFilesBox.getChildren().addAll(clicksBox, impressionsBox, serverBox);
        submitCancelBox.getChildren().addAll(okButton, cancelButton);
    }

    /*
     * Each upload-file field
     */
    class csvUploadBox extends VBox {
    	
    	private String filePath;
    	private String labelName;
    	
    	public csvUploadBox(String labelName) {
    		this.labelName = labelName;
    		
    		this.setAlignment(Pos.CENTER);
    		this.setSpacing(5);
    		
    		build();
    	}
    	
    	private void build() {

    		//The label above each button that indicates the required file
    		Label requiredFileLabel = new Label(labelName + " File");
    		requiredFileLabel.setTextFill(Color.WHITE);
    		
    		//Create an input stream to fetch images
    		InputStream uploadIconStream = this.getClass().getResourceAsStream("/img/download_48px.png");
    		InputStream csvIconStream = this.getClass().getResourceAsStream("/img/csv_40px.png");
    		
            //Create images with ImageView for more flexibility
            ImageView uploadIcon = new ImageView();
            ImageView csvIcon = new ImageView();
            
            uploadIcon.setImage(new Image(uploadIconStream));
            csvIcon.setImage(new Image(csvIconStream));
            
            uploadIcon.setVisible(true);
            csvIcon.setVisible(false);
            
            //The button to upload a file
            Button uploadButton = new Button();
            uploadButton.getStyleClass().add("testStyle");
            uploadButton.setGraphic(uploadIcon);
            
            //The name of the file selected, under the button
            Label fileNameLabel = new Label();
            fileNameLabel.setTextFill(Color.WHITE);

            //The file name and the CSV icon in a separate VBox to tweak spacing
            var fileSelected = new VBox();
            fileSelected.setAlignment(Pos.CENTER);
            fileSelected.setSpacing(0);
            fileSelected.getChildren().addAll(fileNameLabel, csvIcon);

            // add clicksLabel ,clicksButton and status indicator controls to a hbox
            this.getChildren().addAll(requiredFileLabel, uploadButton, fileSelected);

            uploadButton.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.csv", List.of("*.csv")));
                
                // if the work folder path exists
                if (workFolderPath != null) {
                    File dir = new File(workFolderPath);
                    
                    // use the folder as initial directory
                    if (dir.exists()) {
                        fileChooser.setInitialDirectory(dir);
                    }
                }
                File file = fileChooser.showOpenDialog(null);
                
                if (file != null) {
                    workFolderPath = file.getParentFile().getAbsolutePath();
                    filePath = file.getAbsolutePath();
                    
                    fileNameLabel.setText(file.getName());
                    csvIcon.setVisible(true);
                }
            });
    	}
    }
}
