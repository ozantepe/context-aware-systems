package com.dto;

import com.database.feature.GeoObject;
import com.database.feature.GeoObjectPart_Point;

import java.awt.*;

import static com.database.feature.GeoObjectPartType.UNDEFINED;

public class PositionPOI extends GeoObject {

    public PositionPOI(String id, int type, int x, int y) {
        super(id, type);
        this.addPart(new GeoObjectPart_Point(new Point(x, y), UNDEFINED));
    }
}
