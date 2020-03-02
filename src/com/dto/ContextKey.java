package com.dto;

public enum ContextKey {

    POSITION("position"), //
    TEMPERATURE("temperature"), //
    TIME("time"), //
    VELOCITY("velocity"), //
    FREQUENCY("frequency"), //
    RAINPROBABILITY("rainprobability"), //
    WINDVELOCITY("windvelocity"), //
    FUELLEVEL("fuellevel");

    private String mText;

    ContextKey(String text) {
        mText = text;
    }

    @Override
    public String toString() {
        return mText;
    }

}
