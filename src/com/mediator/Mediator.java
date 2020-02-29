package com.mediator;

import com.component.GPSComponent;
import com.component.IComponent;
import com.component.POIComponent;
import com.component.gis.GISComponent;
import com.database.server.IGeoServer;
import com.database.server.OSMGeoServer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public void notify(IComponent sender, String message, Object data) {
        String nameOfSender = sender.getName();
        switch (nameOfSender) {
            case "GIS":
            case "GPS":
            case "POI": {
                GISComponent gisComponent = (GISComponent) components.stream().filter(component -> component instanceof GISComponent).collect(Collectors.toList()).get(0);
                gisComponent.update("sending PoiObjects from POI component to GIS component", data);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void registerComponent(IComponent component) {
        components.add(component);
    }
}
