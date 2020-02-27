package com.component;

import com.mediator.IMediator;
import javafx.scene.layout.Pane;

public class GPSComponent implements IComponent {

    private IMediator mediator;

    private Pane view;
    private final String name = "GPS";

    public GPSComponent(IMediator mediator) {
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

    @Override
    public void update(String message, Object data) {

    }
}
