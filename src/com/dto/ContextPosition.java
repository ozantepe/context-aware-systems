package com.dto;

public class ContextPosition extends ContextElement {

    private double latitude;
    private double longitude;

    public ContextPosition() {
    }

    public ContextPosition(int contextId, ContextKey contextKey, String unit, double latitude, double longitude) {
        super(contextId, contextKey, unit);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int getValueAsInt() {
        throw new RuntimeException("getValueAsInt is not implemented!");
    }
}
