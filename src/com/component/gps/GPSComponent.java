package com.component.gps;

import com.MessageType;
import com.component.IComponent;
import com.component.gps.data.INMEAUpdate;
import com.component.gps.data.NMEAInfo;
import com.component.gps.ui.DataView;
import com.component.gps.ui.SatView;
import com.component.gps.ui.TimeView;
import com.mediator.IMediator;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class GPSComponent implements IComponent, INMEAUpdate {

    private IMediator mediator;

    private Pane view;
    private final String name = "GPS";

    private NMEAParser mNMEAParser;
    private static final String NMEA_FILE_NAME = "src/com/component/gps/NMEA-data-1.log";
    private static final String NMEA_FILTER = "$GPGGA";
    private static final int NMEA_DELAY = 100;

    public GPSComponent(IMediator mediator) {
        this.mediator = mediator;
        initView();
    }

    private void initView() {
        BorderPane mainPane = new BorderPane();

        BorderPane innerPane = new BorderPane();

        VBox vBox = new VBox(8);

        Button button = new Button("Start simulation");
        button.setOnAction(event -> start(innerPane));
        vBox.getChildren().add(button);

        button = new Button("Stop simulation");
        button.setOnAction(event -> stop());
        vBox.getChildren().add(button);

        mainPane.setCenter(innerPane);
        mainPane.setBottom(vBox);
        view = mainPane;
    }

    public void start(BorderPane pane) {
        mNMEAParser = new NMEAParser(NMEA_FILE_NAME, NMEA_FILTER, NMEA_DELAY);
        mNMEAParser.addListener(this);
        initSimulationView(pane);
    }

    private void initSimulationView(BorderPane root) {
        // set up time information window
        TimeView time = new TimeView();
        mNMEAParser.addListener(time);
        time.init();
        root.setTop(time);

        // set up data information window
        DataView data = new DataView();
        mNMEAParser.addListener(data);
        data.init();
        root.setBottom(data);

        // set up satellite information window
        SatView sat = new SatView();
        mNMEAParser.addListener(sat);
        sat.init();
        root.setCenter(sat);
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
    public void update(NMEAInfo data) {
        if (data != null) {
            mediator.notify(this, MessageType.FROM_GPS, data);
        }
    }

    public void stop() {
        mNMEAParser.close();
    }
}
