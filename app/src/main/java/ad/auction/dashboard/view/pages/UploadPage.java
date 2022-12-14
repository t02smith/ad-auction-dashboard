package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.model.files.FileType;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

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

    //Directory of last selected file
    protected String workingDirectory;

    //New campaign details
    protected String campaignName = "";
    protected String clkPath = "";
    protected String impPath = "";
    protected String svrPath = "";

    protected HBox submitCancel;
    protected VBox rows;

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

        // user vbox as the root layout
        this.rows = new VBox();
        rows.setSpacing(25);
        rows.setAlignment(Pos.CENTER);

        //File Inputs
        HBox fileInputs = new HBox(
                uploadFile("Click Log", FileType.CLICK),
                uploadFile("Impressions Log", FileType.IMPRESSION),
                uploadFile("Server Log", FileType.SERVER));
        fileInputs.setAlignment(Pos.CENTER);
        fileInputs.setSpacing(25);

        //submit/cancel buttons
        this.submitCancel = new HBox(submit(), cancel());
        submitCancel.setAlignment(Pos.CENTER);
        submitCancel.setSpacing(25);

        rows.getChildren().addAll(nameInput(), fileInputs, submitCancel);

        mainPane.setCenter(rows);
        mainPane.getStyleClass().add("upload-list");

    }

    /**
     * Send the user an alert on screen
     * @param msg The alert's content
     */
    private void alertUser(String msg) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hint");
        alert.setHeaderText("Warning");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private TextField nameInput() {
        TextField campaignNameInput = new TextField();
        campaignNameInput.setAlignment(Pos.CENTER);
        campaignNameInput.setPromptText("Campaign name:");
        campaignNameInput.setFocusTraversable(false);
        campaignNameInput.setMaxWidth(300);
        campaignNameInput.textProperty().addListener(e -> this.campaignName = campaignNameInput.getText());
        campaignNameInput.setText(campaignName);
        campaignNameInput.getStyleClass().add("upload-text");
        return campaignNameInput;
    }

    private VBox uploadFile(String titleStr, FileType type) {
        final Label title = new Label(titleStr);
        final ImageView csvIcon = new ImageView(new Image(this.getClass().getResourceAsStream("/img/csv_40px.png")));
        final ImageView uploadIcon = new ImageView(new Image(this.getClass().getResourceAsStream("/img/download_48px.png")));

        final Label chosenFile = new Label();
        final Button uploadBtn = new Button();
        uploadBtn.getStyleClass().add("upload-btn");

        switch(type) {
            case IMPRESSION -> {
                chosenFile.setText(impPath);
                if (!impPath.isEmpty()) uploadBtn.setGraphic(csvIcon);
                else uploadBtn.setGraphic(uploadIcon);
            }
            case CLICK -> {
                chosenFile.setText(clkPath);
                if (!clkPath.isEmpty()) uploadBtn.setGraphic(csvIcon);
                else uploadBtn.setGraphic(uploadIcon);
            }
            case SERVER -> {
                chosenFile.setText(svrPath);
                if (!svrPath.isEmpty()) uploadBtn.setGraphic(csvIcon);
                else uploadBtn.setGraphic(uploadIcon);
            }
        }

        uploadBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.csv", List.of("*.csv")));

            if (this.workingDirectory != null) chooser.setInitialDirectory(new File(workingDirectory));
            File file = chooser.showOpenDialog(null);
            if (file != null) {
                this.workingDirectory = file.getParentFile().getAbsolutePath();
                chosenFile.setText(file.getName());
                switch (type) {
                    case IMPRESSION -> this.impPath = file.getAbsolutePath();
                    case SERVER -> this.svrPath = file.getAbsolutePath();
                    case CLICK -> this.clkPath = file.getAbsolutePath();
                }
                uploadBtn.setGraphic(csvIcon);
            }
        });

        var uploadComponents = new VBox(title, uploadBtn, chosenFile);
        uploadComponents.setAlignment(Pos.CENTER);

        return uploadComponents;
    }

    private Button submit() {
        Button submit = new Button("Submit");
        submit.getStyleClass().add("buttonStyle");

        submit.setOnAction(event -> {
            // check if the input is ok
            if (!campaignName.isEmpty() && !impPath.isEmpty()
                    && !clkPath.isEmpty() && !svrPath.isEmpty()) {
                var res = controller.newCampaign(campaignName,
                        clkPath, impPath, svrPath);

                while (!res.isDone()) {}
                try {
                    boolean[] out = res.get();
                    if (out[0] && out[1] && out[2]) window.startMenu();
                    else {
                        var b = new StringBuilder();
                        b.append("The following files have incorrect formats: ");
                        if (!out[0]) b.append(clkPath).append(", ");
                        if (!out[1]) b.append(impPath).append(", ");
                        if (!out[2]) b.append(svrPath);

                        this.alertUser(b.toString());

                        controller.removeCampaign(campaignName);
                    }

                } catch (Exception ignored) {}
            } else { // If the input is faulty, display Alert
                this.alertUser("Please enter all campaign details");
            }
        });

        return submit;
    }

    private Button cancel() {
        Button cancel = new Button("Cancel");
        cancel.getStyleClass().add("buttonStyle");

        cancel.setOnAction(event -> window.startMenu());

        cancel.setOnAction(event -> window.startMenu());

        return cancel;
    }




}
