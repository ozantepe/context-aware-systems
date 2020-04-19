package com.dto;

public enum ContextKey {
    POSITION("Position"),
    TEMPERATURE("Temperature"),
    TIME("Time"),
    VELOCITY("Velocity"),
    FREQUENCY("Frequency"),
    FUEL_LEVEL("FuelLevel");

    private String text;

    ContextKey(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
