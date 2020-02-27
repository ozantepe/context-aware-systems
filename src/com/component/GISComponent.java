package com.component;

import com.mediator.IMediator;
import javafx.scene.layout.Pane;

public class GISComponent implements IComponent {

    private IMediator mediator;

    private Pane view;
    private final String name = "MAP";

    public GISComponent(IMediator mediator) {
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
