package ad.auction.dashboard.view.pages;

import ad.auction.dashboard.App;
import ad.auction.dashboard.view.components.CampaignList;
import ad.auction.dashboard.view.ui.Window;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.concurrent.Future;


public class MainMenuPage extends BasePage {

    private boolean campaignListOpen = false;
    private CampaignList cl;

    public MainMenuPage(Window window) {super(window);}

    @Override
    public void build() {
        var bp = new BorderPane();
        bp.getStyleClass().add("bg-primary");

        var content = new VBox(title(), menuOptions());
        content.setAlignment(Pos.CENTER);
        content.setSpacing(50);
        bp.setCenter(content);

        root.getChildren().add(bp);

        bp.setOnMouseClicked(e -> {
            if (campaignListOpen) {
                this.root.getChildren().remove(cl);
                this.campaignListOpen = false;
            }
        });
    }

    private HBox title() {
        final ImageView logo = new ImageView(new Image(this.getClass().getResource("/img/logo.png").toExternalForm()));
        logo.setPreserveRatio(true);
        logo.setFitWidth(260);

        final Label titleLn1 = new Label("Ad Auction");
        titleLn1.getStyleClass().add("title-text");

        final Label titleLn2 = new Label("Dashboard");
        titleLn2.getStyleClass().addAll("title-text", "text-secondary");
        titleLn2.setPadding(new Insets(-15, 0, 0, 0));

        final VBox titleText = new VBox(titleLn1, titleLn2);
        titleText.setAlignment(Pos.CENTER);
        titleText.setSpacing(0);

        final HBox title = new HBox(logo, titleText);
        title.setSpacing(20);
        title.setAlignment(Pos.CENTER);

        return title;

    }

    private HBox menuOptions() {

        var options = new HBox(
                menuBtn("+", "Upload\nCampaign", e -> window.openUploadPage()),
                menuBtn("L", "Load\nCampaign", e -> openCampaignList()),
                menuBtn("S", "Settings\n  ", e -> settings()),
                menuBtn ("?", "Help\n ", e -> help())
        );
        options.setAlignment(Pos.CENTER);
        options.setSpacing(25);

        return options;
    }

    private VBox menuBtn(String iconText, String description, EventHandler<ActionEvent> onClick) {
        var btn = new Button(iconText);
        btn.getStyleClass().addAll("menu-btn");
        btn.setOnAction(onClick);

        var text = new Label(description);
        text.setTextAlignment(TextAlignment.CENTER);
        text.getStyleClass().addAll("menu-text");

        var content = new VBox(btn, text);
        content.setAlignment(Pos.CENTER);
        content.setSpacing(5);

        return content;
    }

    private void openCampaignList() {
        this.campaignListOpen = true;
        this.cl = new CampaignList(App.getInstance().controller().getCampaigns(), name -> {
            window.openLoadPage(name);

            Task<Void> task = new Task<Void>() {

                @Override
                public Void call() {
                    Future<Void> res = App.getInstance().controller().openCampaign(name);
                    while (!res.isDone()) {}
                    return null;
                }
            };

            task.setOnSucceeded((ee) -> window.openCampaignPage(name));

            new Thread(task).start();
        }, name -> window.openEditPage(name, window::startMenu), () -> {
            this.root.getChildren().remove(cl);
            this.campaignListOpen = false;
        });
        this.root.getChildren().add(cl);
    }

    private void settings() {
        window.openSettingPage();
    }

    private void help() {}
}