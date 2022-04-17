package ad.auction.dashboard.view.settings;

import ad.auction.dashboard.App;
import ad.auction.dashboard.controller.Controller;
import ad.auction.dashboard.model.calculator.Metrics;
import ad.auction.dashboard.view.components.SettingsSection;
import ad.auction.dashboard.view.components.SettingsSectionItem;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.*;

import java.util.Arrays;
import java.util.function.Consumer;

public class CampaignSettings extends SettingPage {

    protected final Controller controller = App.getInstance().controller();

    public CampaignSettings() {
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

    /**
     * Build the campaign list section
     * @return campaign list section
     */
    private SettingsSection campaignList() {
        var section = new SettingsSection("Your Campaigns");
        controller.getCampaigns().forEach(c -> {

            //TODO edit
            var edit = new Button("Edit");
            edit.getStyleClass().add("buttonStyle");
            edit.setOnAction(e -> App.getInstance().window().openEditPage(c.name(), () -> App.getInstance().window().openSettingPage()));

            var del = new Button("Delete");
            del.getStyleClass().add("buttonStyle");
            del.setOnAction(e -> controller.removeCampaign(c.name()));

            var options = new HBox(edit, del);
            options.setSpacing(10);

            section.addChild(new SettingsSectionItem(c.name(), options));
        });

        return section;
    }

    /**
     * Build the campaign options section of the settings menu
     * @return the campaign options section
     */
    private SettingsSection campaignOptions() {
        var section = new SettingsSection("Campaign Options");

        //Default metric
        ComboBox<Metrics> metricList = new ComboBox<>(FXCollections.observableArrayList(Metrics.values()));
        metricList.setValue(controller.getDefaultMetric());
        metricList.setOnAction(e -> controller.setDefaultMetric(metricList.getValue()));

        section.addChild(new SettingsSectionItem(
                "Default metric",
                "The metric calculated when opening a campaign",
                metricList));

        //TODO time resolution

        //Factor
        ComboBox<Integer> factorList = new ComboBox<>(FXCollections.observableList(Arrays.asList(1,2,3,4,5)));
        factorList.setValue(controller.getFactor());
        factorList.setOnAction(e -> controller.setFactor(factorList.getValue()));

        section.addChild(new SettingsSectionItem(
                "Points per unit of time",
                "Having this setting high will impact performance",
                factorList));

        return section;
    }


}
