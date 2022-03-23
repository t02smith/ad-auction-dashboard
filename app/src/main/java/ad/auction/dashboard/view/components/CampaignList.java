package ad.auction.dashboard.view.components;

import ad.auction.dashboard.model.campaigns.Campaign;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.function.Consumer;

public class CampaignList extends BorderPane {

    private final List<Campaign.CampaignData> campaigns;

    private final Consumer<String> openCampaign;
    private final Runnable close;

    public CampaignList(List<Campaign.CampaignData> campaigns, Consumer<String> openCampaign, Runnable close) {
        this.campaigns = campaigns;
        this.openCampaign = openCampaign;
        this.close = close;
        this.build();
    }

    public void build() {
        this.getStyleClass().add("campaign-list");
        this.setMaxWidth(500);
        this.setMaxHeight(500);

        var title = new Label("Your campaigns");
        title.getStyleClass().add("cl-title");
        var titleWrapper = new HBox(title);
        titleWrapper.setAlignment(Pos.CENTER);

        this.setTop(titleWrapper);

        var campaignLs = new VBox();
        this.setCenter(campaignLs);

        campaigns.forEach(c -> {
            var campaign = new Label(c.name());
            campaign.getStyleClass().add("cl-campaign");
            campaign.setOnMouseClicked(e -> openCampaign.accept(c.name()));

            campaignLs.getChildren().add(campaign);
        });

        var close = new Button("Close");
        close.setOnAction(e -> this.close.run());
        close.getStyleClass().add("cl-close");

        this.setBottom(close);
    }
}
