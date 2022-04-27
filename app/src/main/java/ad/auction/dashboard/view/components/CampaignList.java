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

public class CampaignList extends BorderPane {

    private final List<Campaign.CampaignData> campaigns;

    private final Consumer<String> openCampaign;
    private final Consumer<String> editCampaign;
    private final Runnable close;

    public CampaignList(List<Campaign.CampaignData> campaigns, Consumer<String> openCampaign, Consumer<String> editCampaign, Runnable close) {
        this.campaigns = campaigns;
        this.openCampaign = openCampaign;
        this.editCampaign = editCampaign;
        this.close = close;
        this.build();
    }

    public void build() {
        this.getStyleClass().add("campaign-list");
        this.setMaxWidth(500);
        this.setMaxHeight(500);

        this.setPadding(new Insets(10));

        var title = new Label("Your campaigns");
        title.getStyleClass().add("cl-title");
        var titleWrapper = new BorderPane();
        titleWrapper.setCenter(title);

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

        var close = new Button("X");
        close.setOnAction(e -> this.close.run());
        close.getStyleClass().add("cl-close");

        titleWrapper.setRight(close);
    }
}
