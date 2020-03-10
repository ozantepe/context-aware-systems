package com.dto;

public class ContextTime extends ContextElement {

    private int hours;
    private int minutes;
    private int seconds;

    public ContextTime() {
    }

    public ContextTime(int contextId, ContextKey contextKey, String unit, int hours, int minutes, int seconds) {
        super(contextId, contextKey, unit);
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isNight() {
        return hours >= 18 || hours < 6;
    }

    @Override
    public int getValueAsInt() {
        throw new RuntimeException("getValueAsInt is not implemented!");
    }
}
