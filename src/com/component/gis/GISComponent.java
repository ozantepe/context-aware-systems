package com.component.gis;

import com.component.IComponent;
import com.component.IObserver;
import com.database.server.IGeoServer;
import com.dto.MessageType;
import com.mediator.IMediator;
import javafx.scene.layout.Pane;

public class GISComponent implements IComponent, IObserver {

    private IMediator mediator;

    private final String name = "GIS";

    private GISModel model;
    private GISController controller;
    private GISView view;

    public GISComponent(IMediator mediator, IGeoServer geoServer) {
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
    public void update(MessageType messageType, Object data) {
        switch (messageType) {
            case FROM_POI: {
                controller.updatePOI(data);
                break;
            }
            case FROM_GPS: {
                controller.updateGPS(data);
                break;
            }
            case FROM_CM: {
                controller.updateCM(data);
                break;
            }
            default:
                break;
        }
    }
}
