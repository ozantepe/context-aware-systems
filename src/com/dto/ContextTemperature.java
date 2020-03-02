package com.dto;

public class ContextTemperature extends ContextElement {

    private double mTemperature;

    public ContextTemperature() {
    }

    public ContextTemperature(int contextId, ContextKey contextKey, String unit, double temperature) {
        super(contextId, contextKey, unit);
        mTemperature = temperature;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        this.mTemperature = temperature;
    }

    @Override
    public int getValueAsInt() {
        return (int) mTemperature;
    }

    @Override
    public String toString() {
        return "ContextTemperature{" +
                "mTemperature=" + mTemperature +
                ", mContextKey=" + mContextKey +
                ", mContextId=" + mContextId +
                ", mUnit='" + mUnit + '\'' +
                '}';
    }
}
