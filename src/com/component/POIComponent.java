package com.component;

import com.mediator.IMediator;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class POIComponent implements IComponent {

    private IMediator mediator;

    private Pane view;
    private final String name = "POI";

    public POIComponent(IMediator mediator) {
        this.mediator = mediator;
        initView();
    }

    private void initView() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(20, 0, 20, 20));
        view = new Pane();

        Button sendButton = new Button("Send");
        VBox vBox = new VBox(8);

        CheckBox checkBox = new CheckBox("Schools");
        vBox.getChildren().add(checkBox);

        checkBox = new CheckBox("Restaurants");
        vBox.getChildren().add(checkBox);


        checkBox = new CheckBox("Hospitals");
        vBox.getChildren().add(checkBox);

        mainPane.setCenter(vBox);
        mainPane.setBottom(sendButton);
        view = mainPane;
    }

    @Override
    public Pane getView() {
        return view;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(String message, Object data) {

    }
}
