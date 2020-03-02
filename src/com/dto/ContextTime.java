package com.dto;

public class ContextTime extends ContextElement {

    private int mHours;
    private int mMinutes;
    private int mSeconds;

    public ContextTime() {
    }

    public ContextTime(int contextId, ContextKey contextKey, String unit, int hours, int minutes, int seconds) {
        super(contextId, contextKey, unit);
        mHours = hours;
        mMinutes = minutes;
        mSeconds = seconds;
    }

    public int getHours() {
        return mHours;
    }

    public void setHours(int hours) {
        mHours = hours;
    }

    public int getMinutes() {
        return mMinutes;
    }

    public void setMinutes(int minutes) {
        mMinutes = minutes;
    }

    public int getSeconds() {
        return mSeconds;
    }

    public void setSeconds(int seconds) {
        mSeconds = seconds;
    }

    public boolean isNight() {
        return mHours >= 18 || mHours < 6;
    }

    @Override
    public int getValueAsInt() {
        throw new RuntimeException("getValueAsInt is not implemented!");
    }

    @Override
    public String toString() {
        return "ContextTime{" +
                "mHours=" + mHours +
                ", mMinutes=" + mMinutes +
                ", mSeconds=" + mSeconds +
                ", mContextKey=" + mContextKey +
                ", mContextId=" + mContextId +
                ", mUnit='" + mUnit + '\'' +
                '}';
    }
}
