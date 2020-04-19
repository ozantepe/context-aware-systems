package com.database.feature;

import com.database.utilities.Matrix;

import java.awt.*;

public class GeoObjectPart_Linestring extends AGeoObjectPart {

  Point[] mPoints;

  public GeoObjectPart_Linestring(Point[] _pts, GeoObjectPartType _type) {
    mPoints = _pts;
    mType = _type;
  }

  @Override
  protected Rectangle calcBounds() {
    if (mPoints != null && mPoints.length > 0) {
      Point p = mPoints[0];
      Rectangle rect = new Rectangle(p.x, p.y, 0, 0);
      for (Point pt : mPoints) {
        rect.add(pt);
      }
      return rect;
    }
    return null;
  }

  @Override
  protected Polygon calcFillGeometry() {
    return null; // lines can not be filled ...
  }

  @Override
  protected void paintBorder(Graphics _g, Matrix _m, Color _c) {
    _g.setColor(_c);
    for (int i = 1; i < mPoints.length; i++) {
      Point s = _m.multiply(mPoints[i - 1]);
      Point e = _m.multiply(mPoints[i]);
      _g.drawLine(s.x, s.y, e.x, e.y);
    }
  }
}
