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
}
