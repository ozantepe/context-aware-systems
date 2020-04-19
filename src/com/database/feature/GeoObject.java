package com.database.feature;

import com.database.utilities.Matrix;

import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * The class is a first very "rudimentary" representation
 * of a geo object
 */
public class GeoObject {

    /// the id of the object
    protected String mId;
    /// the type of the object
    protected int mType;
    /// the geometry of the object
    protected List<AGeoObjectPart> mParts = null;
    /// the bounding box of the geo objects geometry
    private Rectangle mBounds = null;
    /// list of polygons (not supporting holes) to support drawing holes
    private List<Polygon> mFillGeometry;

    private boolean isActivePOI = false;

    public boolean isActivePOI() {
        return isActivePOI;
    }

    public void setActivePOI(boolean activePOI) {
        isActivePOI = activePOI;
    }

    /**
     * Constructor
     *
     * @param _id   The id of the object
     * @param _type the type of the object
     */
    public GeoObject(String _id, int _type) {
        mId = _id;
        mType = _type;
    }

    public void addPart(AGeoObjectPart _part) {
        if (mParts == null) {
            mParts = new Vector<>();
        }
        mParts.add(_part);
    }

    /**
     * Returns the ID of the Object
     *
     * @return the ID of the Object
     * @see String
     */
    public String getId() {
        return mId;
    }

    /**
     * Returns the type (river, wood, ...) of the object
     *
     * @return the type of the object
     */
    public int getType() {
        return mType;
    }

//  /**
//   * Returns the geometry of the object (in this case only area type objects are supported
//   *
//   * @return the geometry of the object
//   * @see java.awt.Polygon
//   */
//  public Polygon getGeometry() { return mParts; }

    /**
     * Returns the minimum bounding box of the object
     *
     * @return the minimium bounding box of the object in form of a rectangle
     * @see Rectangle
     */
    public Rectangle getBounds() {
        if (mBounds == null && mParts.size() > 0) {
            mBounds = mParts.get(0).getBounds();
            for (AGeoObjectPart part : mParts) {
                mBounds = mBounds.union(part.getBounds());
            }
        }
        return mBounds;
    }

    /**
     * Checks if a given point (world coordinate system) lies inside the object
     *
     * @return true, if the point is inside the object
     * @see Point
     */
    public boolean contains(Point _pt) {
        if (mBounds == null) {
            mBounds = getBounds();
        }
        return mBounds.contains(_pt);
    }


    /**
     * The object can use the given drawing context to paint (resp. represent) itself
     *
     * @param _g      a drawing context used to paint the object
     * @param _matrix the current transformation matrix used to transfer the world coordinate
     *                system based points of the object into the window coordinate based coordinate
     *                space
     * @see Graphics
     */
    public void paint(Graphics _g, Matrix _matrix, Color _fill, Color _border) {
        if (_fill != null) {
            if (mFillGeometry == null) {
                // calculate fill geometry ...
                mFillGeometry = new Vector<>();
                for (AGeoObjectPart part : mParts) {
                    mFillGeometry.add(part.calcFillGeometry());
                }
            }
            _g.setColor(_fill);
            for (Polygon poly : mFillGeometry) {
                if (poly != null) { // line and point-based objects can not be filled ...
                    Polygon temp = _matrix.multiply(poly);
                    _g.fillPolygon(temp);
                }
            } // for polygons ...
        } // fill
        if (_border != null) {
            for (AGeoObjectPart part : mParts) {
                part.paintBorder(_g, _matrix, _border);
            } // for parts
        } // border
    }

    /**
     * Returns the internal information of the object in form of an string
     *
     * @return a string representation of the object
     */
    public String toString() {
        return "OBJ[ " + mId + " ;\t " + mType + "\t POLY ]";
    }
}