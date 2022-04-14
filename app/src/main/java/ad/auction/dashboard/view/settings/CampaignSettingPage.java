package ad.auction.dashboard.view.settings;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.model.campaigns.Campaign;
import ad.auction.dashboard.view.components.SettingsSection;
import ad.auction.dashboard.view.components.SettingsSectionItem;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.List;

public class CampaignSettingPage extends SettingPage {

    protected final Controller controller = App.getInstance().controller();
    public CampaignSettingPage() {
        super("Campaign List");
        this.updateView();
    }

    @Override
    public void updateView() {
        this.setCenter(new VBox(
                campaignList(),
                campaignOptions()
        ));
    }

    private SettingsSection campaignList() {
        var section = new SettingsSection("Your Campaigns");
        controller.getCampaigns().forEach(c -> {

            //TODO edit

            var del = new Button("Delete");
            del.getStyleClass().add("buttonStyle");
            del.setOnAction(e -> controller.removeCampaign(c.name()));

            var options = new HBox(del);

            section.addChild(new SettingsSectionItem(c.name(), options));
        });

        return section;
    }

    private SettingsSection campaignOptions() {
        var section = new SettingsSection("Campaign Options");

        //Default metric
        ComboBox<Metrics> metricList = new ComboBox<>(FXCollections.observableArrayList(Metrics.values()));
        metricList.setValue(controller.getDefaultMetric());
        metricList.setOnAction(e -> {
            controller.setDefaultMetric(metricList.getValue());
        });

        section.addChild(new SettingsSectionItem("Default Metric", metricList));

        //TODO time resolution

        //TODO factor

        return section;
    }


}
