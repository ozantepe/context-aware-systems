package com.dto;

public class ContextPosition extends ContextElement {

    private double mLatitude;
    private double mLongitude;

    public ContextPosition() {
    }

    public ContextPosition(int contextId, ContextKey contextKey, String unit, double latitude, double longitude) {
        super(contextId, contextKey, unit);
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    @Override
    public int getValueAsInt() {
        throw new RuntimeException("getValueAsInt is not implemented!");
    }

    @Override
    public String toString() {
        return "ContextPosition{" +
                "mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mContextKey=" + mContextKey +
                ", mContextId=" + mContextId +
                ", mUnit='" + mUnit + '\'' +
                '}';
    }
}
