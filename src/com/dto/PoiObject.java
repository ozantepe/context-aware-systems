package com.dto;

import java.awt.image.BufferedImage;

public class PoiObject {

    private BufferedImage icon;
    private String name;
    private String latitude;
    private String longitude;
    private PoiType poiType;

    public PoiObject(BufferedImage icon, String name, String latitude, String longitude, PoiType poiType) {
        this.icon = icon;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.poiType = poiType;
    }

    public PoiType getPoiType() {
        return poiType;
    }

    public void setPoiType(PoiType poiType) {
        this.poiType = poiType;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public void setIcon(BufferedImage icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    enum PoiType {
        SCHOOL,
        RESTAURANT,
        HOSPITAL
    }
}