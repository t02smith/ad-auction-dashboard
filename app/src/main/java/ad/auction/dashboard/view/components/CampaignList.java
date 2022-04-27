package ad.auction.dashboard.view.components;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;
import java.util.function.Consumer;

/**
 * List of campaigns that can be opened
 *
 * @author tcs1g20
 */
public class CampaignList extends BorderPane {

    private final List<Campaign.CampaignData> campaigns;

    private final Consumer<String> openCampaign;
    private final Consumer<String> editCampaign;
    private final Runnable close;

    /**
     * Create a new campaign list
     * @param campaigns The list of possible campaigns
     * @param openCampaign Function to open a campaign
     * @param editCampaign Function to edit a campaign
     * @param close Function to close campaign list
     */
    public CampaignList(List<Campaign.CampaignData> campaigns, Consumer<String> openCampaign, Consumer<String> editCampaign, Runnable close) {
        this.campaigns = campaigns;
        this.openCampaign = openCampaign;
        this.editCampaign = editCampaign;
        this.close = close;
        this.build();
    }

    /**
     * Build the campaign list
     */
    public void build() {
        this.getStyleClass().add("campaign-list");
        this.setMaxWidth(500);
        this.setMaxHeight(500);

        this.setPadding(new Insets(10));

        var title = new Label("Your campaigns");
        title.getStyleClass().add("cl-title");
        var titleWrapper = new HBox(title);
        titleWrapper.setAlignment(Pos.CENTER);

        this.setTop(titleWrapper);

        var campaignLs = new VBox();
        campaignLs.setPadding(new Insets(20));
        this.setCenter(campaignLs);

        campaigns.forEach(c -> {
            var empty = new Region();
            HBox.setHgrow(empty, Priority.ALWAYS);

            var campaign = new Label(c.name());
            campaign.getStyleClass().add("cl-campaign");
            campaign.setOnMouseClicked(e -> openCampaign.accept(c.name()));

            var settings = new ImageView(new Image(this.getClass().getResource("/img/settings.png").toExternalForm()));
            settings.setPreserveRatio(true);
            settings.setFitWidth(35);
            settings.getStyleClass().add("settings-icon");
            settings.setOnMouseClicked(e -> editCampaign.accept(c.name()));

            campaignLs.getChildren().add(new HBox(campaign, empty, settings));
        });

        var close = new Button("Close");
        close.setOnAction(e -> this.close.run());
        close.getStyleClass().add("cl-close");

        this.setBottom(close);
    }
}
