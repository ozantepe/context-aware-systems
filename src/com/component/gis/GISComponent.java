package com.component.gis;

import com.component.IComponent;
import com.database.server.OSMGeoServer;
import com.mediator.IMediator;
import com.mediator.Mediator;
import javafx.scene.layout.Pane;

public class GISComponent implements IComponent {

    private IMediator mediator;

    private final String name = "MAP";

    private GISModel model;
    private GISController controller;
    private GISView view;

    public GISComponent(Mediator mediator) {
        this.mediator = mediator;
        initComponent();
    }

    private void initComponent() {
        model = new GISModel();
        controller = new GISController(model, new OSMGeoServer());
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
        System.out.println("gis sent request");
        mediator.notify(this, message, data);
    }
}
