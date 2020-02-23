package com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;

public class Mediator extends Application implements IMediator, IObserver {

    @Override
    public void start(Stage stage) throws Exception {
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(BufferedImage bufferedImage) {

    }

    @Override
    public void registerGISComponent(GISComponent gisComponent) {

    }

    @Override
    public void registerGPSComponent(GPSComponent gpsComponent) {

    }

    @Override
    public void registerPOIComponent(POIComponent poiComponent) {

    }
}
