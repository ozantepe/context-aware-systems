package com.dto;

public class ContextVelocity extends ContextElement {

    private double velocity;

    public ContextVelocity() {
    }

    public ContextVelocity(int contextId, ContextKey contextKey, String unit, double velocity) {
        super(contextId, contextKey, unit);
        this.velocity = velocity;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    @Override
    public int getValueAsInt() {
        return (int) velocity;
    }
}
