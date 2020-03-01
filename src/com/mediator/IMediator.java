package com.mediator;

import com.MessageType;
import com.component.IComponent;

public interface IMediator {

    void notify(IComponent sender, MessageType messageType, Object data);

    void registerComponent(IComponent component);
}
