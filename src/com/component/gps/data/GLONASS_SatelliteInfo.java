package com.component.gps.data;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Class that manages the drawing of GLONASS satellites on the satellite view (SatView)
 *
 * <p>Satellites are represented by filled rounded rectangles and black text/border
 *
 * @author jkroesche
 * @see SatView
 */
public class GLONASS_SatelliteInfo extends ASatelliteInfo {

  private static final int RADIUS = 20;
  private static final double ARC_WIDTH = 7;
  private static final double ARC_HEIGHT = 7;

  public GLONASS_SatelliteInfo(int _id, int _v, int _h, int _s) {
    super(_id, _v, _h, _s);
  }

  @Override
  protected void drawSatellite(GraphicsContext _gc, int _x, int _y) {
    _gc.fillRoundRect(
            _x - (RADIUS >> 1), _y - (RADIUS >> 1), RADIUS, RADIUS, ARC_WIDTH, ARC_HEIGHT);
    _gc.setStroke(Color.BLACK);
    _gc.strokeRoundRect(
            _x - (RADIUS >> 1), _y - (RADIUS >> 1), RADIUS, RADIUS, ARC_WIDTH, ARC_HEIGHT);
    Font f = new Font("Courier", 14);

    //	Text temp = new Text(String.valueOf(getID()));
    //	temp.setFont(f);
    //	Bounds bbox = temp.getBoundsInLocal();

    _gc.setFont(f);
    _gc.setTextBaseline(VPos.CENTER);
    _gc.setTextAlign(TextAlignment.CENTER);
    _gc.strokeText(String.valueOf(getID()), _x, _y - 1);
  }
}
