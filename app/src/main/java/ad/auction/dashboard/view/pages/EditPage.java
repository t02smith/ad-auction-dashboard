package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.model.campaigns.Campaign.CampaignData;
import ad.auction.dashboard.view.ui.Window;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;

/**
 * Creates the campaign editing page
 */
public class EditPage extends UploadPage {

    private final CampaignData oldCampaign;

    //Method on where to return to after finish editing
    private final Runnable returnTo;


    public EditPage(Window window, String name, Runnable returnTo) {
        super(window);
        campaignName = name;
        oldCampaign = controller.getCampaignData(name);
        this.returnTo = returnTo;
    }

    /**
     * Builds the UI element of the editing page
     */
    @Override
    public void build() {
        this.clkPath = new File(oldCampaign.clkPath()).getName();
        this.svrPath = new File(oldCampaign.svrPath()).getName();
        this.impPath = new File(oldCampaign.impPath()).getName();
        this.campaignName = oldCampaign.name();

        super.build();

        this.rows.getChildren().remove(submitCancel);

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
            controller.editCampaign(oldCampaign.name(), campaignName, clkPath, impPath, svrPath);
            this.returnTo.run();
        });

        cancelButton.setOnAction(event -> this.returnTo.run());

        var saveCancel = new HBox(saveButton, cancelButton);
        saveCancel.setAlignment(Pos.CENTER);
        saveCancel.setSpacing(25);

        deleteButton.setOnAction(event -> {
            controller.removeCampaign(oldCampaign.name());
            window.startMenu();
        });

        this.rows.getChildren().addAll(saveCancel, deleteButton);
    }

}
