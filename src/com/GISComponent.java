package com;

import javafx.scene.layout.Pane;

public class GISComponent implements IComponent {

    private IMediator mediator;

    public GISComponent(IMediator mediator) {
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
