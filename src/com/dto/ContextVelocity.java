package com.dto;

public class ContextVelocity extends ContextElement {

    private double mVelocity;

    public ContextVelocity() {
    }

    public ContextVelocity(int contextId, ContextKey contextKey, String unit, double velocity) {
        super(contextId, contextKey, unit);
        mVelocity = velocity;
    }

    public double getVelocity() {
        return mVelocity;
    }

    public void setVelocity(double velocity) {
        mVelocity = velocity;
    }

    @Override
    public int getValueAsInt() {
        return (int) mVelocity;
    }

    @Override
    public String toString() {
        return "ContextVelocity{" +
                "mVelocity=" + mVelocity +
                ", mContextKey=" + mContextKey +
                ", mContextId=" + mContextId +
                ", mUnit='" + mUnit + '\'' +
                '}';
    }
}
