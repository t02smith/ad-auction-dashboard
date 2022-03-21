package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.campaigns.Campaign.CampaignData;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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

public class EditPage extends BasePage{
    private static final Logger logger = LogManager.getLogger(UploadPage.class.getSimpleName());
    private String workFolderPath = null;
    private final Controller controller = App.getInstance().controller();
    private String impressionString;
    private String clickString;
    private String serverString;
    private String campaignName;
    private CampaignData oldCampaign;

    private static final int LABEL_WIDTH = 120;
    private static final int BUTTON_WIDTH = 160;
    private static final int H_GAP = 10;
    private static final int V_GAP = 10;


    public EditPage(Window window, String name) {
        super(window);
        campaignName = name;
        oldCampaign = controller.getCampaignData();
    }

    @Override
    public void build() {
        logger.info("Building edit page");

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
        var mainMenuText = new Text("Edit Graph Options");
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
        campaignsLabel.setTextFill(Color.WHITE);
        TextField campaignsNameText = new TextField();
        campaignsNameText.setText(campaignName);
        campaignsNameText.setPromptText("Campaign name:");
        campaignsNameText.setFocusTraversable(false);
        campaignsNameText.setPrefWidth(200);
        HBox hBox1 = new HBox(campaignsNameText);
        hBox1.setSpacing(H_GAP);
        hBox1.setAlignment(Pos.CENTER_LEFT);
        rowsBox.getChildren().add(hBox1);
        VBox.setVgrow(hBox1, Priority.ALWAYS);

        // impression file selection
        Label impressionsLabel = new Label("Impression file:");
        impressionsLabel.setTextFill(Color.WHITE);
        //added a String to remember file path
        impressionString = oldCampaign.impPath();
        File impressionFile = new File(impressionString);
        workFolderPath = impressionFile.getParentFile().getAbsolutePath();
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
        icon1.setVisible(true);

        ImageView icon2 = new ImageView();
        icon2.setImage(csvIconImage);
        icon2.setVisible(true);

        ImageView icon3 = new ImageView();
        icon3.setImage(csvIconImage);
        icon3.setVisible(true);

        //Change to a label type
        Label impressionsText = new Label();
        impressionsText.setTextFill(Color.WHITE);
        impressionsText.setText(impressionFile.getName());
        //Change finish
        Button impressionsButton = new Button();
        impressionsButton.getStyleClass().add("testStyle");
        impressionsButton.setGraphic(iconButton1);
        VBox pane1 = new VBox(impressionsText, icon1);
        pane1.setAlignment(Pos.CENTER_LEFT);

        impressionsLabel.setMinWidth(LABEL_WIDTH);
        impressionsButton.setMinWidth(BUTTON_WIDTH);
        // add impressionsLabel ,impressionsButton and status indicator controls to a hbox
        VBox vBox2 = new VBox(impressionsLabel, impressionsButton, pane1);
        vBox2.setSpacing(H_GAP);
        // vertical center alignment
        vBox2.setAlignment(Pos.CENTER_LEFT);
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
        //added a String to remember file path
        clickString = oldCampaign.clkPath();
        File clickFile = new File(clickString);
        //added finish
        //Change to a label type
        Label clicksText = new Label();
        clicksText.setTextFill(Color.WHITE);
        clicksText.setText(clickFile.getName());
        //Change finish
        VBox pane2 = new VBox(clicksText, icon2);
        pane2.setAlignment(Pos.CENTER_LEFT);
        Button clicksButton = new Button();
        clicksButton.getStyleClass().add("testStyle");
        clicksButton.setGraphic(iconButton2);
        clicksLabel.setMinWidth(LABEL_WIDTH);
        clicksButton.setMinWidth(BUTTON_WIDTH);
        // add clicksLabel ,clicksButton and status indicator controls to a hbox
        VBox vBox3 = new VBox(clicksLabel, clicksButton, pane2);
        vBox3.setSpacing(H_GAP);
        // vertical center alignment
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
                clickString = file.getAbsolutePath();
                icon2.setVisible(true);
            }
        });

        // server file selection
        Label serverLabel = new Label("Server file:");
        serverLabel.setTextFill(Color.WHITE);
        //added a String to remember file path
        serverString = oldCampaign.svrPath();
        File serverFile = new File(serverString);
        //added finish
        //Change to a label type
        Label serverText = new Label();
        serverText.setTextFill(Color.WHITE);
        serverText.setText(serverFile.getName());
        //Change finish
        VBox pane3 = new VBox(serverText, icon3);
        pane3.setAlignment(Pos.CENTER_LEFT);
        Button serverButton = new Button();
        serverButton.getStyleClass().add("testStyle");
        serverButton.setGraphic(iconButton3);
        serverLabel.setMinWidth(LABEL_WIDTH);
        serverButton.setMinWidth(BUTTON_WIDTH);
        // add serverLabel ,serverButton and status indicator controls to a hbox
        VBox vBox4 = new VBox(serverLabel, serverButton, pane3);
        //hBox4.setSpacing(H_GAP);
        vBox4.setSpacing(H_GAP);
        // vertical center alignment
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
            if (file != null) {
                // update work folder path
                workFolderPath = file.getParentFile().getAbsolutePath();
                serverText.setText(file.getName());
                serverString = file.getAbsolutePath();
                icon3.setVisible(true);
            }
        });

        //Save, cancel and delete buttons
        Button saveButton = new Button("Save");
        saveButton.getStyleClass().add("buttonStyle");
        Button cancelButton = new Button("Cancel");
        cancelButton.getStyleClass().add("buttonStyle");
        Button deleteButton = new Button("Delete Campaign");
        deleteButton.getStyleClass().add("buttonStyle");
        deleteButton.setStyle("-fx-background-color: #ff0000");

        //button actions
        saveButton.setOnAction(event -> {
            controller.editCampaign(oldCampaign.name(), campaignsNameText.getText(), clickString, impressionString, serverString);
            window.startMenu();
        });

        cancelButton.setOnAction(event -> {
            window.openCampaignPage(campaignName);
        });

        deleteButton.setOnAction(event -> {
            controller.removeCampaign(oldCampaign.name());
            window.startMenu();
        });

        saveButton.setMinWidth(BUTTON_WIDTH);
        cancelButton.setMinWidth(BUTTON_WIDTH);
        deleteButton.setMinWidth(2*BUTTON_WIDTH + 60 + LABEL_WIDTH);
        // add okButton and cancelButton to a hbox
        HBox hBox5 = new HBox(saveButton, cancelButton);
        hBox5.setPadding(new Insets(0, 0, 0, 0));
        hBox5.setSpacing(60 + LABEL_WIDTH);
        //Add these to a vbox above the delete button
        var buttonBox = new VBox(hBox5, deleteButton);
        buttonBox.setSpacing(10);
        // vertical center alignment
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        //Test
        rowsBox.getChildren().add(colsBox);
        rowsBox.getChildren().add(buttonBox);

        borderPane.setTop(title);
        borderPane.setCenter(rowsBox);
        borderPane.getStyleClass().add("upload-list");
    }
}
