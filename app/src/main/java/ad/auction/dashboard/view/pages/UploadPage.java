package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import ad.auction.dashboard.controller.Controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * The upload file page view class
 */
public class UploadPage extends BasePage {
	
    private static final Logger logger = LogManager.getLogger(UploadPage.class.getSimpleName());
    private String workFolderPath = null;
    private final Controller controller = App.getInstance().controller();
    public String impressionString;
    public String clickString;
    public String serverString;
    public String testString;

    private static final int LABEL_WIDTH = 120;
    private static final int BUTTON_WIDTH = 160;
    private static final int H_GAP = 10;
    private static final int V_GAP = 10;


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

        // create a grid pane layout the controls
        // user vbox as the root layout
        VBox rowsBox = new VBox();
        rowsBox.setMinWidth(550);
        rowsBox.setMinHeight(200);
        rowsBox.setMaxWidth(550);
        rowsBox.setMaxHeight(200);
        rowsBox.setSpacing(V_GAP);
        
        // Test
        HBox colsBox = new HBox();
        colsBox.setMinWidth(550);
        colsBox.setMinHeight(200);
        colsBox.setMaxWidth(550);
        colsBox.setMaxHeight(200);
        colsBox.setSpacing(V_GAP);

        // campaign name input
        Label campaignsLabel = new Label("Campaign name:");
        //Test
        campaignsLabel.setTextFill(Color.WHITE);
        //Test finish
        TextField campaignsNameText = new TextField();
        campaignsNameText.setPromptText("Campaign name:");
        campaignsNameText.setFocusTraversable(false);
        if (!campaignsNameText.isFocused()){
             logger.info("The camPaignsNameText is focused");
        }
        //campaignsLabel.setPrefWidth(LABEL_WIDTH);
        campaignsNameText.setPrefWidth(200);
        // add campaignsLabel and campaignsNameText to a hbox
        //HBox hBox1 = new HBox(campaignsLabel, campaignsNameText);
        HBox hBox1 = new HBox(campaignsNameText);
        hBox1.setSpacing(H_GAP);
        // vertical center alignment
        hBox1.setAlignment(Pos.CENTER_LEFT);
        rowsBox.getChildren().add(hBox1);
        VBox.setVgrow(hBox1, Priority.ALWAYS);


        // impression file selection
        Label impressionsLabel = new Label("Impression file:");
        impressionsLabel.setTextFill(Color.WHITE);
        //TextField impressionsText = new TextField("");
        //added a String to remember file path
        impressionString = new String();
        //added finish

        // create an input stream from resource
        InputStream iconStream = this.getClass().getResourceAsStream("/img/csv_40px.png");
        // create a image from stream
        Image csvIconImage = new Image(iconStream);
        //create an input stream for button from resource
        InputStream buttonStream = this.getClass().getResourceAsStream("/img/download_48px.png");
        Image buttonIconImage = new Image(buttonStream);
        //create one icon, set visible
        ImageView iconButton1 = new ImageView();
        iconButton1.setImage(buttonIconImage);
        iconButton1.setVisible(true);
        ImageView iconButton2 = new ImageView();
        iconButton2.setImage(buttonIconImage);
        iconButton2.setVisible(true);
        ImageView iconButton3 = new ImageView();
        iconButton3.setImage(buttonIconImage);
        iconButton3.setVisible(true);

        // create three icon, and set them invisible
        ImageView icon1 = new ImageView();
        icon1.setImage(csvIconImage);
        icon1.setVisible(false);

        ImageView icon2 = new ImageView();
        icon2.setImage(csvIconImage);
        icon2.setVisible(false);

        ImageView icon3 = new ImageView();
        icon3.setImage(csvIconImage);
        icon3.setVisible(false);


        //Change to a label type
        //Label impressionsText = new Label("impressions");
        Label impressionsText = new Label();
        impressionsText.setTextFill(Color.WHITE);
        //Change finish
        //Button impressionsButton = new Button("Select file");
        Button impressionsButton = new Button();
        impressionsButton.getStyleClass().add("testStyle");
        impressionsButton.setGraphic(iconButton1);
        //HBox pane1 = new HBox(impressionsText, icon1);
        VBox pane1 = new VBox(impressionsText, icon1);
        pane1.setAlignment(Pos.CENTER_LEFT);

        impressionsLabel.setMinWidth(LABEL_WIDTH);
        impressionsButton.setMinWidth(BUTTON_WIDTH);
        // add impressionsLabel ,impressionsButton and status indicator controls to a hbox
        //HBox hBox2 = new HBox(impressionsLabel, impressionsButton, pane1);
        VBox vBox2 = new VBox(impressionsLabel, impressionsButton, pane1);
        vBox2.setSpacing(H_GAP);
        // vertical center alignment
        vBox2.setAlignment(Pos.CENTER_LEFT);
        //rowsBox.getChildren().add(vBox2);
        colsBox.getChildren().add(vBox2);


        impressionsButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.csv", List.of("*.csv")));
            // if the work folder path exists
            if (workFolderPath != null) {
                File dir = new File(workFolderPath);
                if (dir.exists()) {
                    // use the folder as initial directory
                    fileChooser.setInitialDirectory(dir);
                }
            }
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                // update work folder path
                workFolderPath = file.getParentFile().getAbsolutePath();
                impressionsText.setText(file.getName());
                impressionString = file.getAbsolutePath();
                icon1.setVisible(true);
            }
        });

        // clicks file selection
        Label clicksLabel = new Label("Clicks file:");
        clicksLabel.setTextFill(Color.WHITE);
        //TextField clicksText = new TextField("");
        //added a String to remember file path
        clickString = new String();
        //added finish
        //Change to a label type
        //Label clicksText = new Label("clicks");
        Label clicksText = new Label();
        clicksText.setTextFill(Color.WHITE);
        //Change finish
        //HBox pane2 = new HBox(clicksText, icon2);
        VBox pane2 = new VBox(clicksText, icon2);
        pane2.setAlignment(Pos.CENTER_LEFT);
        //Button clicksButton = new Button("Select file");
        Button clicksButton = new Button();
        clicksButton.getStyleClass().add("testStyle");
        clicksButton.setGraphic(iconButton2);
        clicksLabel.setMinWidth(LABEL_WIDTH);
        clicksButton.setMinWidth(BUTTON_WIDTH);
        // add clicksLabel ,clicksButton and status indicator controls to a hbox
        // HBox hBox3 = new HBox(clicksLabel, clicksButton, pane2);
        VBox vBox3 = new VBox(clicksLabel, clicksButton, pane2);
        // hBox3.setSpacing(H_GAP);
        vBox3.setSpacing(H_GAP);
        // vertical center alignment
        //hBox3.setAlignment(Pos.CENTER_LEFT);
        vBox3.setAlignment(Pos.CENTER_LEFT);
        colsBox.getChildren().add(vBox3);

        clicksButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.csv", List.of("*.csv")));
            // if the work folder path exists
            if (workFolderPath != null) {
                File dir = new File(workFolderPath);
                if (dir.exists()) {
                    // use the folder as initial directory
                    fileChooser.setInitialDirectory(dir);
                }
            }

            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                workFolderPath = file.getParentFile().getAbsolutePath();
                clicksText.setText(file.getName());
                //clicksText.setText(file.getAbsolutePath());
                clickString = file.getAbsolutePath();
                icon2.setVisible(true);
            }
        });

        // server file selection
        Label serverLabel = new Label("Server file:");
        serverLabel.setTextFill(Color.WHITE);
        //TextField serverText = new TextField("");
        //added a String to remember file path
        serverString = new String();
        //added finish
        //Change to a label type
        //Label serverText = new Label("servers");
        Label serverText = new Label();
        serverText.setTextFill(Color.WHITE);
        //Change finish
        //HBox pane3 = new HBox(serverText, icon3);
        VBox pane3 = new VBox(serverText, icon3);
        pane3.setAlignment(Pos.CENTER_LEFT);
        //Button serverButton = new Button("Select file");
        Button serverButton = new Button();
        serverButton.getStyleClass().add("testStyle");
        serverButton.setGraphic(iconButton3);
        serverLabel.setMinWidth(LABEL_WIDTH);
        serverButton.setMinWidth(BUTTON_WIDTH);
        // add serverLabel ,serverButton and status indicator controls to a hbox
        //HBox hBox4 = new HBox(serverLabel, serverButton, pane3);
        VBox vBox4 = new VBox(serverLabel, serverButton, pane3);
        //hBox4.setSpacing(H_GAP);
        vBox4.setSpacing(H_GAP);
        // vertical center alignment
        //hBox4.setAlignment(Pos.CENTER_LEFT);
        vBox4.setAlignment(Pos.CENTER_LEFT);
        colsBox.getChildren().add(vBox4);


        serverButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.csv", List.of("*.csv")));
            // if the work folder path exists
            if (workFolderPath != null) {
                File dir = new File(workFolderPath);
                if (dir.exists()) {
                    // use the folder as initial directory
                    fileChooser.setInitialDirectory(dir);
                }
            }

            File file = fileChooser.showOpenDialog(null);
            //File file = fileChooser.showSaveDialog(ownerWindow)
            if (file != null) {
                // update work folder path
                workFolderPath = file.getParentFile().getAbsolutePath();
                serverText.setText(file.getName());
                //serverText.setText(file.getAbsolutePath());
                serverString = file.getAbsolutePath();
                icon3.setVisible(true);
            }
        });

        // submit and cancel button
        Button okButton = new Button("Submit");
        okButton.getStyleClass().add("buttonStyle");
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("buttonStyle");
        okButton.setOnAction(event -> {
            // check if the input is ok
            if (!campaignsNameText.getText().isEmpty() && !impressionString.isEmpty() 
            		&& !clickString.isEmpty() && !serverString.isEmpty()) {
                controller.newCampaign(campaignsNameText.getText(), 
                		clickString, impressionString, serverString);
                
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

        cancelButton.setOnAction(event -> {
            window.startMenu();
        });

        okButton.setMinWidth(BUTTON_WIDTH);
        cancelButton.setMinWidth(BUTTON_WIDTH);
        // add okButton and cancelButton to a hbox
        HBox hBox5 = new HBox(okButton, cancelButton);
        hBox5.setPadding(new Insets(0, 0, 0, 0));
        hBox5.setSpacing(60 + LABEL_WIDTH);
        // vertical center alignment
        hBox5.setAlignment(Pos.CENTER_LEFT);
        //Test
        rowsBox.getChildren().add(colsBox);
        rowsBox.getChildren().add(hBox5);

        mainPane.setTop(title);
        mainPane.setCenter(rowsBox);
        mainPane.getStyleClass().add("upload-list");

    }

    /*
     * Each upload-file field
     */
    class csvUploadBox extends VBox {
    	
    	private String filePath;
    	private final String labelName;
    	
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
