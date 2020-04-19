package com.database.feature;

import com.database.utilities.Matrix;

import java.awt.*;

public class GeoObjectPart_Point extends AGeoObjectPart {

    private static final int DELTA = 4;
    private Point mPoint;

    public GeoObjectPart_Point(Point _pt, GeoObjectPartType _type) {
        mPoint = _pt;
        mType = _type;
    }

    @Override
    protected Rectangle calcBounds() {
        return new Rectangle(mPoint.x, mPoint.y, 0, 0);
    }

    @Override
    protected Polygon calcFillGeometry() {
        return null; // points can not be filled
    }

    @Override
    protected void paintBorder(Graphics _g, Matrix _m, Color _c) {
        Point p = _m.multiply(mPoint);
        _g.setColor(_c);
        _g.drawOval(p.x - (DELTA >> 1), p.y - (DELTA >> 1), DELTA, DELTA);
    }
}
