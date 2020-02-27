package com.mediator;

import com.component.IComponent;

public interface IMediator {

    void notify(IComponent sender, String message, Object data);

    void registerComponent(IComponent component);
}
