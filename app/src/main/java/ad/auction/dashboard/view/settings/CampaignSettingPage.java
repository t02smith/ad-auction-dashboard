package ad.auction.dashboard.view.settings;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.List;

public class CampaignSettingPage extends SettingPage {
    // get controller
    protected final Controller controller = App.getInstance().controller();
    // root layout
    private VBox itemListBox;


    public CampaignSettingPage() {
        super("Campaign List");
        // create list box
        itemListBox = new VBox();
        // set spacing
        itemListBox.setSpacing(20);
        this.setCenter(itemListBox);
    }

    @Override
    public void updateView() {
        // clear list
        itemListBox.getChildren().clear();
        // get campaigns
        List<Campaign.CampaignData> campaigns = controller.getCampaigns();
        for (Campaign.CampaignData campaign : campaigns) {
            // build each row
            Pane pane = buildItem(campaign);
            // add to list box
            itemListBox.getChildren().add(pane);
        }
    }

    private Pane buildItem(Campaign.CampaignData campaignData) {
        // get campaign name
        String name = campaignData.name();
        // add delete button
        Button delButton = new Button("Del");
        delButton.getStyleClass().add("buttonStyle");
        // add label
        Label label = new Label(name);
        label.getStyleClass().add("settingLabel");
        BorderPane pane = new BorderPane();
        // add to border pane
        pane.setLeft(label);
        pane.setRight(delButton);
        // delete event handler
        delButton.setOnAction(event -> {
            // remove campaign
            controller.removeCampaign(name);
            // update list
            updateView();
        });

        return pane;
    }


}
