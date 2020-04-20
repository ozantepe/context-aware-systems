package com.dto;

import java.util.HashMap;
import java.util.Map;

public class ContextSituation {

  private Map<ContextKey, ContextElement> contextElements = new HashMap<>();

  public void addContext(ContextElement contextElement) {
    contextElements.put(contextElement.getContextKey(), contextElement);
  }

  public Map<ContextKey, ContextElement> getContextElements() {
    return contextElements;
  }

  public ContextElement getContextElement(ContextKey contextKey) {
    if (contextElements.containsKey(contextKey)) {
      return contextElements.get(contextKey);
    }
    return null;
  }

  public Map<String, Object> getContextElementsAsRuleMap() {
    Map<String, Object> contextElements = new HashMap<>();

    ContextElement tmpElem;
    tmpElem = getContextElement(ContextKey.TEMPERATURE);
    if (tmpElem != null) {
      contextElements.put("current_temp", tmpElem.getValueAsInt());
    }

    tmpElem = getContextElement(ContextKey.FUEL_LEVEL);
    if (tmpElem != null) {
      contextElements.put("current_fuel_status", tmpElem.getValueAsInt());
    }

    tmpElem = getContextElement(ContextKey.TIME);
    if (tmpElem instanceof ContextTime) {
      ContextTime time = (ContextTime) tmpElem;

      if (time.isNight()) {
        contextElements.put("current_time", "Night");
      } else {
        contextElements.put("current_time", "Day");
      }
    }
    return contextElements;
  }
}
