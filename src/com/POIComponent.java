package com;

import javafx.scene.layout.Pane;

public class POIComponent implements IComponent {

    private IMediator mediator;

    public POIComponent(IMediator mediator) {
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
