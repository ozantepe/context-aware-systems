package com.component.gps.data;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * Class that manages the drawing of Galileo satellites
 * on the satellite view (SatView)
 * <p>
 * Satellites are represented by filled hexagons and white text/border
 *
 * @author jkroesche
 * @see SatView
 */
public class Galileo_SatelliteInfo extends ASatelliteInfo {

    private static final int RADIUS = 10;

    public Galileo_SatelliteInfo(int _id, int _v, int _h, int _s) {
        super(_id, _v, _h, _s);
    }

    @Override
    protected void drawSatellite(GraphicsContext _gc, int _x, int _y) {
        double[] xValues = new double[]{
                _x + RADIUS,
                _x + (RADIUS >> 1),
                _x - (RADIUS >> 1),
                _x - RADIUS,
                _x - RADIUS,
                _x - (RADIUS >> 1),
                _x + (RADIUS >> 1),
                _x + RADIUS,
                _x + RADIUS};
        double[] yValues = new double[]{
                _y + (RADIUS >> 1),
                _y + RADIUS,
                _y + RADIUS,
                _y + (RADIUS >> 1),
                _y - (RADIUS >> 1),
                _y - RADIUS,
                _y - RADIUS,
                _y - (RADIUS >> 1),
                _y + (RADIUS >> 1)};

        _gc.fillPolygon(xValues, yValues, xValues.length);
        _gc.setStroke(Color.WHITE);
        _gc.strokePolygon(xValues, yValues, xValues.length);

        Font f = new Font("Courier", 14);

//		Text temp = new Text(String.valueOf(getID()));
//		temp.setFont(f);
//		Bounds bbox = temp.getBoundsInLocal();

        _gc.setFont(f);
        _gc.setTextBaseline(VPos.CENTER);
        _gc.setTextAlign(TextAlignment.CENTER);
        _gc.strokeText(String.valueOf(getID()),
                _x,
                _y - 1);
    }
}
