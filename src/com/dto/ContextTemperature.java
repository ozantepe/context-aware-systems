package com.dto;

public class ContextTemperature extends ContextElement {

    private double temperature;

    public ContextTemperature() {
    }

    public ContextTemperature(int contextId, ContextKey contextKey, String unit, double temperature) {
        super(contextId, contextKey, unit);
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public int getValueAsInt() {
        return (int) temperature;
    }
}
