package ad.auction.dashboard.view.components;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class BackBtn extends StackPane {

    public BackBtn(EventHandler<ActionEvent> onClick) {
        var backButton = new Button();
        backButton.getStyleClass().add("buttonStyle");
        backButton.setMaxWidth(50);
        backButton.setMaxHeight(40);
        var buttonWidth = backButton.getMaxWidth();
        var buttonHeight = backButton.getMaxHeight();
        backButton.setOnAction(onClick);


        // Draw arrow with canvas
        var canvas = new Canvas(buttonWidth, buttonHeight);
        canvas.setMouseTransparent(true);

        var gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.strokeLine(buttonWidth * 0.25, buttonHeight * 0.5, buttonWidth * 0.75,  buttonHeight * 0.5);
        gc.strokeLine(buttonWidth * 0.25, buttonHeight * 0.5, buttonWidth * 0.5, buttonHeight * 0.725);
        gc.strokeLine(buttonWidth * 0.25, buttonHeight * 0.5, buttonWidth * 0.5, buttonHeight * 0.275);

        this.getChildren().addAll(backButton, canvas);
    }
}
