package com.component.gps.ui;


import com.component.gps.data.INMEAUpdate;
import com.component.gps.data.NMEAInfo;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TimeView extends Pane implements INMEAUpdate {

    /// private constant to access time value field
    private static final String TIME_VALUE_ID = "time-value";

    /**
     * Initialize the layout of the time panel ...
     */
    public void init() {

        setStyle("-fx-background-color:cyan");

        StackPane stack = new StackPane();
        stack.prefHeightProperty().bind(this.heightProperty());
        stack.prefWidthProperty().bind(this.widthProperty());

        Font f = new Font("Courier", 30);

        Text t = new Text("undefined");
        t.setStroke(Color.BLACK);
        t.setFill(Color.WHITE);
        t.setFont(f);
        t.setId(TIME_VALUE_ID);

        stack.getChildren().add(t);

        this.getChildren().add(stack);
    }

    @Override
    public void update(NMEAInfo _data) {
        if (_data != null) {
            // outside main UI-Thread ...
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String time = _data.getTime();
                    Text timeL = (Text) TimeView.this.lookup("#" + TIME_VALUE_ID);
                    timeL.setText(time);
                }
            });
        }
    }
}
