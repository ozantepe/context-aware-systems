package com.database.utilities;

import java.awt.*;

public class Matrix {

  private double matrix11;
  private double matrix12;
  private double matrix13;

  private double matrix21;
  private double matrix22;
  private double matrix23;

  private double matrix31;
  private double matrix32;
  private double matrix33;

  /**
   * Basic constructor
   */
  public Matrix() {
    this(1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0);
  }

  /**
   * Constructor
   */
  public Matrix(
          double a1,
          double a2,
          double a3,
          double b1,
          double b2,
          double b3,
          double c1,
          double c2,
          double c3) {
    matrix11 = a1;
    matrix12 = a2;
    matrix13 = a3;

    matrix21 = b1;
    matrix22 = b2;
    matrix23 = b3;

    matrix31 = c1;
    matrix32 = c2;
    matrix33 = c3;
  }

  /**
   * Copy constructor
   *
   * @param _o the matrix that should be copied
   */
  public Matrix(Matrix _o) {
    matrix11 = _o.matrix11;
    matrix12 = _o.matrix12;
    matrix13 = _o.matrix13;

    matrix21 = _o.matrix21;
    matrix22 = _o.matrix22;
    matrix23 = _o.matrix23;

    matrix31 = _o.matrix31;
    matrix32 = _o.matrix32;
    matrix33 = _o.matrix33;
  }

  /**
   * Generates a clone of the matrix
   */
  public Object clone() {
    return copy();
  }

  /**
   * Generates a copy of the matrix
   */
  public Matrix copy() {
    return new Matrix(this);
  }

  /**
   * Scalar-Multiplication, multiplies the current matrix with a constant value _s
   *
   * @param _s the scalar value
   * @return Die neue Matrix
   */
  public Matrix multiply(double _s) {
    return new Matrix(
            matrix11 * _s,
            matrix12 * _s,
            matrix13 * _s,
            matrix21 * _s,
            matrix22 * _s,
            matrix23 * _s,
            matrix31 * _s,
            matrix32 * _s,
            matrix33 * _s);
  }

  /**
   * Multiplies a point with the matrix and returns the transformed point
   *
   * @param _pt the point that should be multiplied with the matrix
   * @return the transformed (new) point of the multiplication
   */
  public Point multiply(Point _pt) {
    double x = _pt.x * matrix11 + _pt.y * matrix12 + matrix13;
    double y = _pt.y * matrix21 + _pt.y * matrix22 + matrix23;
    return new Point((int) x, (int) y);
  }

  /**
   * Multiplies a rectangle with the matrix and returns the transformed rectangle
   *
   * @param _rect the rectangle that should be multiplied with the matrix
   * @return the transformed (new) rectangle of the multiplication
   */
  public Rectangle multiply(Rectangle _rect) {
    Point lb = new Point(_rect.x, _rect.y);
    Point rt = new Point((int) _rect.getMaxX(), (int) _rect.getMaxY());

    Point convLB = multiply(lb);
    Point convRT = multiply(rt);

    Rectangle rect = new Rectangle(convLB);
    rect.add(convRT);

    return rect;
  }

  /**
   * Multiplies a polygon with the matrix and returns the transformed polygon
   *
   * @param _poly the polygon that should be multiplied with the matrix
   * @return the transformed (new) rectangle of the multiplication
   */
  public Polygon multiply(Polygon _poly) {
    int[] pointsX = new int[_poly.npoints];
    int[] pointsY = new int[_poly.npoints];
    int pointsN = _poly.npoints;
    for (int i = 0; i < pointsN; i++) {
      pointsX[i] =
              (int)
                      (matrix11 * (double) _poly.xpoints[i]
                              + matrix12 * (double) _poly.ypoints[i]
                              + matrix13);
      pointsY[i] =
              (int)
                      (matrix21 * (double) _poly.xpoints[i]
                              + matrix22 * (double) _poly.ypoints[i]
                              + matrix23);
    } // for i
    return new Polygon(pointsX, pointsY, pointsN);
  }

  /**
   * Matrix multiplication. Keep in mind that matrix multiplications are not commutative
   *
   * @param _o the matrix that should be multiplied with this one (A*_O)
   * @return the result matrix of the multiplication
   */
  public Matrix multiply(Matrix _o) {
    Matrix result = new Matrix();

    result.matrix11 = matrix11 * _o.matrix11 + matrix12 * _o.matrix21 + matrix13 * _o.matrix31;
    result.matrix12 = matrix11 * _o.matrix12 + matrix12 * _o.matrix22 + matrix13 * _o.matrix32;
    result.matrix13 = matrix11 * _o.matrix13 + matrix12 * _o.matrix23 + matrix13 * _o.matrix33;

    result.matrix21 = matrix21 * _o.matrix11 + matrix22 * _o.matrix21 + matrix23 * _o.matrix31;
    result.matrix22 = matrix21 * _o.matrix12 + matrix22 * _o.matrix22 + matrix23 * _o.matrix32;
    result.matrix23 = matrix21 * _o.matrix13 + matrix22 * _o.matrix23 + matrix23 * _o.matrix33;

    result.matrix31 = matrix31 * _o.matrix11 + matrix32 * _o.matrix21 + matrix33 * _o.matrix31;
    result.matrix32 = matrix31 * _o.matrix12 + matrix32 * _o.matrix22 + matrix33 * _o.matrix32;
    result.matrix33 = matrix31 * _o.matrix13 + matrix32 * _o.matrix23 + matrix33 * _o.matrix33;
    return result;
  }

  /**
   * Provides a rotation matrix
   *
   * @param _rad the angle in (rad) for the rotation
   * @return the corresponding rotation matrix
   */
  public static Matrix rotate(double _rad) {
    return new Matrix(
            Math.cos(_rad), -Math.sin(_rad), 0, Math.sin(_rad), Math.cos(_rad), 0, 0, 0, 1);
  }

  /**
   * Provides a scaling matrix
   *
   * @param _scale the factor that should be used for scaling
   * @return the corresponding scaling matrix
   */
  public static Matrix scale(double _scale) {
    return new Matrix(_scale, 0, 0, 0, _scale, 0, 0, 0, 1);
  }

  /**
   * Provides a translation matrix
   *
   * @param _x the translation value concerning the x-axis
   * @param _y the translation value concerning the y-axis
   * @return the translation matrix
   */
  public static Matrix translate(double _x, double _y) {
    return new Matrix(1, 0, _x, 0, 1, _y, 0, 0, 1);
  }

  /**
   * Provides a translation matrix
   *
   * @param _p a point that contains the translation values
   * @return the translation matrix
   */
  public static Matrix translate(Point _p) {
    return translate(_p.x, _p.y);
  }

  /**
   * Provides a mirror matrix (y-axis)
   *
   * @return the mirror matrix
   */
  public static Matrix mirrorY() {
    return new Matrix(-1, 0, 0, 0, 1, 0, 0, 0, 1);
  }

  /**
   * Provides a mirror matrix (x-axis)
   *
   * @return the mirror matrix
   */
  public static Matrix mirrorX() {
    return new Matrix(1, 0, 0, 0, -1, 0, 0, 0, 1);
  }

  /**
   * Provides the inverted version of the matrix
   *
   * @return the inverted matrix
   */
  public Matrix invers() {
    double determinante = det();
    if (determinante == 0) {
      // besser waere eine Exception zu schmeissen, um den
      // Anwender/Programmierer darueber zu informieren, dass
      // ein Fehler aufgetreten ist, aber so geht es auch ;-)
      return null;
    }
    double u = 1 / determinante;

    double m11 = det(matrix22, matrix23, matrix32, matrix33);
    double m12 = det(matrix13, matrix12, matrix33, matrix32);
    double m13 = det(matrix12, matrix13, matrix22, matrix23);

    double m21 = det(matrix23, matrix21, matrix33, matrix31);
    double m22 = det(matrix11, matrix13, matrix31, matrix33);
    double m23 = det(matrix13, matrix11, matrix23, matrix21);

    double m31 = det(matrix21, matrix22, matrix31, matrix32);
    double m32 = det(matrix12, matrix11, matrix32, matrix31);
    double m33 = det(matrix11, matrix12, matrix21, matrix22);

    Matrix matrix = new Matrix(m11, m12, m13, m21, m22, m23, m31, m32, m33);
    return matrix.multiply(u);
  }

  /**
   * Calculates the determinant of a 3x3 matrix
   *
   * @return the determinant
   */
  private double det() {
    return (matrix11 * matrix22 * matrix33)
            + (matrix12 * matrix23 * matrix31)
            + (matrix13 * matrix21 * matrix32)
            - (matrix11 * matrix23 * matrix32)
            - (matrix12 * matrix21 * matrix33)
            - (matrix13 * matrix22 * matrix31);
  }

  /**
   * Calculates the determinant of a 2x2 matrix
   *
   * @param _m11 cell(0,0) of the matrix
   * @param _m12 cell(0,1) of the matrix
   * @param _m21 cell(1,0) of the matrix
   * @param _m22 cell(1,1) of the matrix
   * @return the determinant
   */
  public double det(double _m11, double _m12, double _m21, double _m22) {
    return _m11 * _m22 - _m12 * _m21;
  }

  /**
   * Provides a String representation of the matrix
   *
   * @return a String containing the formatted input of the matrix
   */
  public String toString() {
    return matrix11 + "\t" + matrix12 + "\t" + matrix13 + "\n" + matrix21 + "\t" + matrix22 + "\t"
            + matrix23 + "\n" + matrix31 + "\t" + matrix32 + "\t" + matrix33 + "\n";
  }

  /**
   * Provides a matrix that contains all the necessary information (translation, scaling, mirror and
   * translation) to transform objects that are contained in the _map rectangle to be visualized in
   * a rectangle of the size _win
   *
   * @param _map the rectangle in world coordinates
   * @param _win the rectangle in window coordinates
   * @return the matching transformation matrix
   */
  public static Matrix zoomToFit(Rectangle _map, Rectangle _win) {
    double scaleFactor = getZoomFactor(_map, _win);

    Matrix translateA = Matrix.translate(-_map.getCenterX(), -_map.getCenterY());
    Matrix scale = Matrix.scale(scaleFactor);
    Matrix mirrorX = Matrix.mirrorX();
    Matrix translateB = Matrix.translate(_win.getCenterX(), _win.getCenterY());

    // Matrizen in umgekehrter Reihenfolge zusammenmultiplizieren

    return translateB.multiply(mirrorX.multiply(scale.multiply(translateA)));
  }

  /**
   * Provides a matrix that extends the provided matrix _matrix in a way that a scaling
   * transformation at the dedicated point is conducted.
   *
   * @param _matrix the matrix that needs to be extended
   * @param _p      the point that defines the zoom point
   * @param _scale  the zoom factor
   * @return the manipulated matrix
   */
  public static Matrix zoomToPoint(Matrix _matrix, Point _p, double _scale) {
    // auf den 0-Punkt verschieben ...
    Matrix t1 = Matrix.translate(-_p.x, -_p.y);
    // auf die richtige Groesse zoomen ...
    Matrix z = Matrix.scale(_scale);
    // zurueck zum Punkt verschieben ...
    Matrix t2 = Matrix.translate(_p.x, _p.y);
    // Matrizen in umgekehrter Reihenfolge zusammenmultiplizieren
    return t2.multiply(z).multiply(t1).multiply(_matrix);
  }

  /**
   * Provides a matrix that extends the provided matrix _matrix in a way that a translation
   * transformation is added.
   *
   * @param _matrix the matrix that needs to be extended
   * @param _dx     the translation factor concerning the x-axis
   * @param _dy     the translation factor concerning the y-axis
   * @return the manipulated matrix
   */
  public static Matrix drag(Matrix _matrix, int _dx, int _dy) {
    Matrix t = Matrix.translate(_dx, _dy);
    return t.multiply(_matrix);
  }

  /**
   * Calculates the scale factor of two provided rectangles (how much to scale the one to fit the
   * bounds of the other)
   *
   * @param _map the map rectangle
   * @param _win the window rectangle
   * @return the scaling factor
   * @see Rectangle
   */
  private static double getZoomFactor(Rectangle _map, Rectangle _win) {
    if (_map == null || _win == null) {
      // besser waere eine Exception zu schmeissen, um den
      // Anwender/Programmierer darueber zu informieren, dass
      // ein Fehler aufgetreten ist.
      return 1.0;
    }
    double scaleX = _map.width != 0 ? (double) _win.width / (double) _map.width : 1.0;
    double scaleY = _map.height != 0 ? (double) _win.height / (double) _map.height : 1.0;
    // Skalierungsfaktor ist der kleinere von beiden
    return Math.min(scaleX, scaleY);
  }
}
