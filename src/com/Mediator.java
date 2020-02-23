package com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class Mediator extends Application implements IMediator, IObserver {

    private Set<IComponent> components;

    @Override
    public void start(Stage stage) throws Exception {
        initComponents();

        // Tab pane
        TabPane tabPane = new TabPane();

        Tab gisTab = new Tab("MAP");
        gisTab.setClosable(false);
        Tab gpsTab = new Tab("GPS");
        gpsTab.setClosable(false);
        Tab poiTab = new Tab("POI");
        poiTab.setClosable(false);
        tabPane.getTabs().addAll(gisTab, gpsTab, poiTab);

        stage.setTitle("CAS Project");
        stage.setScene(new Scene(tabPane, 640, 480));
        stage.show();
    }

    private void initComponents() {
        components = new HashSet<>();

        // Binding GIS component
        GISComponent gisComponent = new GISComponent(this);
        this.registerGISComponent(gisComponent);

        // Binding GPS component
        GPSComponent gpsComponent = new GPSComponent(this);
        this.registerGPSComponent(gpsComponent);

        // Binding POI component
        POIComponent poiComponent = new POIComponent(this);
        this.registerPOIComponent(poiComponent);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(BufferedImage bufferedImage) {

    }

    @Override
    public void registerGISComponent(GISComponent gisComponent) {
        components.add(gisComponent);
    }

    @Override
    public void registerGPSComponent(GPSComponent gpsComponent) {
        components.add(gpsComponent);
    }

    @Override
    public void registerPOIComponent(POIComponent poiComponent) {
        components.add(poiComponent);
    }
}
