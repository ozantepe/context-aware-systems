package com.database.feature;

import com.database.utilities.Matrix;

import java.awt.*;

public class GeoObjectPart_Area extends AGeoObjectPart {

    Polygon mArea;

    public GeoObjectPart_Area(Polygon _poly, GeoObjectPartType _type) {
        mArea = _poly;
        mType = _type;
    }

    @Override
    protected Rectangle calcBounds() {
        if (mArea != null) return mArea.getBounds();
        return null;
    }

    @Override
    protected Polygon calcFillGeometry() {
        if (mType == GeoObjectPartType.MAIN || mType == GeoObjectPartType.EXCLAVE) {
            // check for holes ...
            if (mHoles.size() == 0) {
                // no holes ...
                return mArea;
            } else {
                // found holes ...
                Polygon poly = new Polygon(
                        mArea.xpoints.clone(),
                        mArea.ypoints.clone(),
                        mArea.npoints);
                for (AGeoObjectPart holePart : mHoles) {
                    if (holePart instanceof GeoObjectPart_Area) {
                        GeoObjectPart_Area part = (GeoObjectPart_Area) holePart;
                        poly = union(poly, part.mArea);
                    } else {
                        System.out.println("GeoObjectPart_Area::calcFillGeometry only area type parts allowed");
                    }
                }
                return poly;
            }
        } else if (mType == GeoObjectPartType.HOLE) {
            return mArea;
        }
        return new Polygon();
    }

    private Polygon union(Polygon _pA, Polygon _pB) {
        int[] index = minDistance(_pA, _pB);
        Polygon result = new Polygon();
        for (int i = 0; i < index[0] + 1; i++) {
            result.addPoint(_pA.xpoints[i], _pA.ypoints[i]);
        }
        for (int i = index[1]; i < _pB.npoints; i++) {
            result.addPoint(_pB.xpoints[i], _pB.ypoints[i]);
        }
        for (int i = 0; i < index[1] + 1; i++) {
            result.addPoint(_pB.xpoints[i], _pB.ypoints[i]);
        }
        for (int i = index[0]; i < _pA.npoints; i++) {
            result.addPoint(_pA.xpoints[i], _pA.ypoints[i]);
        }
        return result;
    }

    private int[] minDistance(Polygon _pA, Polygon _pB) {
        int[] ix = new int[]{0, 0};
        double minDist = Integer.MAX_VALUE;
        for (int i = 0; i < _pA.npoints; i++) {
            Point pA = new Point(_pA.xpoints[i], _pA.ypoints[i]);
            for (int j = 0; j < _pB.npoints; j++) {
                Point pB = new Point(_pB.xpoints[j], _pB.ypoints[j]);

                double dist = Math.sqrt(Math.pow(pA.x - pB.x, 2) + Math.pow(pA.y - pB.y, 2));
                if (dist < minDist) {
                    minDist = dist;
                    ix[0] = i;
                    ix[1] = j;
                } // relaxation
            } // for j
        } // for i
        return ix;
    }

    @Override
    protected void paintBorder(Graphics _g, Matrix _m, Color _c) {
        _g.setColor(_c);
        Polygon temp = _m.multiply(mArea);
        _g.drawPolygon(temp);
        for (AGeoObjectPart part : mHoles) {
            part.paintBorder(_g, _m, _c);
        }
    }

}
