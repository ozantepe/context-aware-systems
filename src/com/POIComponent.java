package com;

import javafx.scene.layout.Pane;

public class POIComponent implements IComponent {

    private IMediator mediator;

    private Pane view;
    private final String name = "POI";

    public POIComponent(IMediator mediator) {
        this.mediator = mediator;
        initView();
    }

    private void initView() {
        view = new Pane();
    }

    @Override
    public Pane getView() {
        return view;
    }

    @Override
    public String getName() {
        return name;
    }
}
