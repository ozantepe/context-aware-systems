package com.dto;

public abstract class ContextElement {

    protected ContextKey contextKey;
    protected int contextId;
    protected String unit;

    public ContextElement() {

    }

    public ContextElement(int contextId, ContextKey contextKey, String unit) {
        this.contextId = contextId;
        this.contextKey = contextKey;
        this.unit = unit;
    }

    public int getContextId() {
        return contextId;
    }

    public void setContextId(int contextId) {
        this.contextId = contextId;
    }

    public ContextKey getContextKey() {
        return contextKey;
    }

    public void setContextKey(ContextKey contextKey) {
        this.contextKey = contextKey;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public abstract int getValueAsInt();
}
