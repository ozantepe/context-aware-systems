package com.component.poi;

import com.component.IComponent;
import com.dto.MessageType;
import com.dto.PoiType;
import com.mediator.IMediator;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class POIComponent implements IComponent {

    private IMediator mediator;

    private Set<PoiType> poiTypes = new HashSet<>();

    private Pane view;
    private final String name = "POI";

    public POIComponent(IMediator mediator) {
        this.mediator = mediator;
        // TODO: poi objects can be read from poi.osm file using a sax reader
        initView();
    }

    private void initView() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(20, 0, 20, 20));

        VBox vBox = new VBox(8);

        CheckBox checkBox = new CheckBox("Police");
        vBox.getChildren().add(checkBox);
        checkBox.setOnAction(event -> {
            if (((CheckBox) event.getSource()).isSelected()) {
                poiTypes.add(PoiType.POLICE);
            } else {
                poiTypes.remove(PoiType.POLICE);
            }
        });

        checkBox = new CheckBox("Bank");
        vBox.getChildren().add(checkBox);
        checkBox.setOnAction(event -> {
            if (((CheckBox) event.getSource()).isSelected()) {
                poiTypes.add(PoiType.BANK);
            } else {
                poiTypes.remove(PoiType.BANK);
            }
        });


        checkBox = new CheckBox("Parking");
        vBox.getChildren().add(checkBox);
        checkBox.setOnAction(event -> {
            if (((CheckBox) event.getSource()).isSelected()) {
                poiTypes.add(PoiType.PARKING);
            } else {
                poiTypes.remove(PoiType.PARKING);
            }
        });

        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendPoiTypes());

        mainPane.setCenter(vBox);
        mainPane.setBottom(sendButton);
        view = mainPane;
    }

    private void sendPoiTypes() {
        Set<Integer> typeIds = poiTypes.stream().map(PoiType::getTypeId).collect(Collectors.toSet());
        mediator.notify(this, MessageType.FROM_POI, typeIds);
    }

    @Override
    public Pane getView() {
        return view;
    }

    @Override
    public String getName() {
        return name;
    }
}
