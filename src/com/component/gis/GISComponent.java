package com.component.gis;

import com.component.IComponent;
import com.database.server.IGeoServer;
import com.mediator.IMediator;
import com.mediator.Mediator;
import javafx.scene.layout.Pane;

public class GISComponent implements IComponent {

    private IMediator mediator;

    private final String name = "GIS";

    private GISModel model;
    private GISController controller;
    private GISView view;

    public GISComponent(Mediator mediator, IGeoServer geoServer) {
        this.mediator = mediator;
        initComponent(geoServer);
    }

    private void initComponent(IGeoServer geoServer) {
        model = new GISModel();
        controller = new GISController(model, geoServer);
        view = new GISView(controller);
        model.addListener(view);
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
        System.out.println(message);
        controller.update(data);
    }
}
