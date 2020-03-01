package com.component;

import com.MessageType;

public interface IObserver {

    void update(MessageType messageType, Object data);
}
