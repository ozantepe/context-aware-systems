package com.mediator;

import com.component.GISComponent;
import com.component.GPSComponent;
import com.component.IComponent;
import com.component.POIComponent;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Mediator extends Application implements IMediator {

    private List<IComponent> components;

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

        // Binding GIS component
        GISComponent gisComponent = new GISComponent(this);
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

    }

    @Override
    public void registerComponent(IComponent component) {
        components.add(component);
    }
}
