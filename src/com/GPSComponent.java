package com;

import javafx.scene.layout.Pane;

public class GPSComponent implements IComponent {

    private IMediator mediator;

    public GPSComponent(IMediator mediator) {
        this.mediator = mediator;
    }

    @Override
    public Pane getView() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
