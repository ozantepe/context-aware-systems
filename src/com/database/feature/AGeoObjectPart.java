package com.database.feature;

import com.database.utilities.Matrix;

import java.awt.*;
import java.util.List;
import java.util.Vector;

public abstract class AGeoObjectPart {

  List<AGeoObjectPart> mHoles = new Vector<>();

  protected GeoObjectPartType mType = GeoObjectPartType.UNDEFINED;

  private Rectangle mBounds;

  public void addPart(AGeoObjectPart _part) {
    if (_part.getType() == GeoObjectPartType.HOLE) {
      mHoles.add(_part);
    } else {
      System.out.println(
              "AGeoObjectPart::addPart() unhandled type encountered --> " + _part.getType().toString());
    }
  }

  public Rectangle getBounds() {
    if (mBounds == null) {
      mBounds = calcBounds();
      for (AGeoObjectPart part : mHoles) {
        mBounds = mBounds.union(part.getBounds());
      }
    }
    return mBounds;
  }

  public GeoObjectPartType getType() {
    return mType;
  }

  protected abstract Rectangle calcBounds();

  protected abstract Polygon calcFillGeometry();

  protected abstract void paintBorder(Graphics _g, Matrix _m, Color _c);
}
