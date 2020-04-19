package com.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ContextTime extends ContextElement {

    private int hours;
    private int minutes;
    private int seconds;

    public ContextTime() {
    }

    public ContextTime(
            int contextId, ContextKey contextKey, String unit, int hours, int minutes, int seconds) {
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
        return hours * 3600 + minutes * 60 + seconds;
    }

    public void initCurrentTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String[] dateStr = sdf.format(date).split(":");
        int hours = Integer.parseInt(dateStr[0]);
        int minutes = Integer.parseInt(dateStr[1]);
        int seconds = Integer.parseInt(dateStr[2]);
        setHours(hours);
        setMinutes(minutes);
        setSeconds(seconds);
    }
}
