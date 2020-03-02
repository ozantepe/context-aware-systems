package com.mediator;

import com.component.IComponent;
import com.dto.MessageType;

public interface IMediator {

    void notify(IComponent sender, MessageType messageType, Object data);

    void registerComponent(IComponent component);
}
