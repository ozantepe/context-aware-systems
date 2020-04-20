package com.mediator;

import com.component.ComponentFactory;
import com.component.IComponent;
import com.component.cm.ContextManagementComponent;
import com.component.gis.GISComponent;
import com.database.server.IGeoServer;
import com.database.server.OSMGeoServer;
import com.dto.MessageType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.List;

public class Mediator extends Application implements IMediator {

  private static String COMPONENT_COMPOSITION_FILE_NAME;

  private static List<IComponent> components;

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
    stage.setOnCloseRequest(event -> System.exit(0));
    stage.show();
  }

  private void initComponents() {
    IGeoServer geoServer = new OSMGeoServer();
    components = ComponentFactory.buildComponents(COMPONENT_COMPOSITION_FILE_NAME, this, geoServer);
  }

  public static void main(String[] args) {
    if (args.length >= 1) {
      COMPONENT_COMPOSITION_FILE_NAME = args[0];
    } else {
      COMPONENT_COMPOSITION_FILE_NAME = "xml/ComponentComposition.xml";
    }
    launch(args);
  }

  @Override
  public void notify(IComponent sender, MessageType messageType, Object data) {
    if (messageType.equals(MessageType.FROM_POI)) {
      // update gis component
      updateGIS(messageType, data);
    } else if (messageType.equals(MessageType.FROM_GPS)) {
      // update gis
      updateGIS(messageType, data);
      updateCM(messageType, data);
    } else if (messageType.equals(MessageType.FROM_AAL)) {
      // update gis and cm components
      updateGIS(messageType, data);
      updateCM(messageType, data);
    } else if (messageType.equals(MessageType.FROM_CM)) {
      // update gis component
      updateGIS(messageType, data);
    } else {
      System.out.print("Unhandled message: ");
    }
    System.out.println(messageType.getMessage());
  }

  private void updateGIS(MessageType messageType, Object data) {
    GISComponent gisComponent =
            (GISComponent)
                    components.stream()
                            .filter(component -> component instanceof GISComponent)
                            .findFirst()
                            .get();
    gisComponent.update(messageType, data);
  }

  private void updateCM(MessageType messageType, Object data) {
    ContextManagementComponent cmComponent =
            (ContextManagementComponent)
                    components.stream()
                            .filter(component -> component instanceof ContextManagementComponent)
                            .findFirst()
                            .get();
    cmComponent.update(messageType, data);
  }

  public static IComponent getComponent(Class componentClass) {
    for (IComponent component : components) {
      if (componentClass.isInstance(component)) {
        return component;
      }
    }
    return null;
  }
}
