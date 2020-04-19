package com.dto;

import com.database.feature.GeoObject;
import com.database.feature.GeoObjectPart_Point;

import java.awt.*;

import static com.database.feature.GeoObjectPartType.UNDEFINED;

public class PositionPOI extends GeoObject {

    private static final int PROJECTION_FACTOR = 1000000;

    private double longitude;
    private double latitude;

    public PositionPOI(String id, int type, double x, double y) {
        super(id, type);
        this.addPart(
                new GeoObjectPart_Point(
                        new Point((int) (x * PROJECTION_FACTOR), (int) (y * PROJECTION_FACTOR)), UNDEFINED));
        this.longitude = x;
        this.latitude = y;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
