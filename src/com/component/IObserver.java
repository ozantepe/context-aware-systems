package com.component;

import com.dto.MessageType;

public interface IObserver {

  void update(MessageType messageType, Object data);
}
