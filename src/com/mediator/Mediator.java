package com.mediator;

import com.MessageType;
import com.component.IComponent;
import com.component.POIComponent;
import com.component.gis.GISComponent;
import com.component.gps.GPSComponent;
import com.component.gps.data.NMEAInfo;
import com.database.feature.GeoObject;
import com.database.feature.GeoObjectPart_Point;
import com.database.server.IGeoServer;
import com.database.server.OSMGeoServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.database.feature.GeoObjectPartType.UNDEFINED;

public class Mediator extends Application implements IMediator {

    private List<IComponent> components;
    private IGeoServer geoServer;

    @Override
    public void start(Stage stage) throws Exception {
        initComponents();

        // Tab pane
        TabPane tabPane = new TabPane();

        for (IComponent component : components) {
            Tab tab = new Tab(component.getName(), component.getView());
            tab.setClosable(false);
            tabPane.getTabs().add(tab);
        }

        stage.setTitle("CAS Project");
        stage.setScene(new Scene(tabPane, 640, 480));
        stage.show();
    }

    private void initComponents() {
        components = new ArrayList<>();
        geoServer = new OSMGeoServer();

        // Binding GIS component
        GISComponent gisComponent = new GISComponent(this, geoServer);
        this.registerComponent(gisComponent);

        // Binding GPS component
        GPSComponent gpsComponent = new GPSComponent(this);
        this.registerComponent(gpsComponent);

        // Binding POI component
        POIComponent poiComponent = new POIComponent(this);
        this.registerComponent(poiComponent);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void notify(IComponent sender, MessageType messageType, Object data) {
        String nameOfSender = sender.getName();
        GISComponent gisComponent = (GISComponent) components.stream().filter(component -> component instanceof GISComponent).collect(Collectors.toList()).get(0);

        if (nameOfSender.equals("POI") && messageType.equals(MessageType.FROM_POI)) {
            gisComponent.update(messageType, data);
            System.out.println(messageType.getMessage());
        } else if (nameOfSender.equals("GPS") && messageType.equals(MessageType.FROM_GPS)) {
            // convert nmea info to geo object
            NMEAInfo nmeaInfo = (NMEAInfo) data;
            GeoObjectPart_Point geoPoint = new GeoObjectPart_Point(
                    new Point((int) nmeaInfo.getLongitude(), (int) nmeaInfo.getLatitude()), UNDEFINED);
            GeoObject geoObject = new GeoObject("userLocation", 9999);
            geoObject.addPart(geoPoint);

            // then send it to gis component
            gisComponent.update(messageType, geoObject);
            System.out.println(messageType.getMessage());
        } else {
            System.out.println("Invalid notification !!!");
        }
    }

    @Override
    public void registerComponent(IComponent component) {
        components.add(component);
    }
}
