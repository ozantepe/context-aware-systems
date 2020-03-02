package com.dto;

public abstract class ContextElement {

    protected ContextKey mContextKey;
    protected int mContextId;
    protected String mUnit;

    public ContextElement() {

    }

    public ContextElement(int contextId, ContextKey contextKey, String unit) {
        mContextId = contextId;
        mContextKey = contextKey;
        mUnit = unit;
    }

    public int getContextId() {
        return mContextId;
    }

    public void setContextId(int contextId) {
        mContextId = contextId;
    }

    public ContextKey getContextKey() {
        return mContextKey;
    }

    public void setContextKey(ContextKey contextKey) {
        mContextKey = contextKey;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        mUnit = unit;
    }

    public abstract int getValueAsInt();

    @Override
    public String toString() {
        return "com.dto.ContextElement{" +
                "mContextKey=" + mContextKey +
                ", mContextId=" + mContextId +
                ", mUnit='" + mUnit + '\'' +
                '}';
    }
}
