package com.component.gps.data;

import javafx.scene.canvas.GraphicsContext;


/**
 * Class that manages the drawing of GNSS satellites
 * on the satellite view (SatView)
 * <p>
 * Satellites are represented by filled rectangles and black text/border
 *
 * @author jkroesche
 * @see SatView
 */

public class GNSS_SatelliteInfo extends ASatelliteInfo {

    private static final int RADIUS = 10;

    public GNSS_SatelliteInfo(int _id, int _v, int _h, int _s) {
        super(_id, _v, _h, _s);
    }

    @Override
    protected void drawSatellite(GraphicsContext _gc, int _dx, int _dy) {
        System.out.println("GNSS satellite found ... ");
        _gc.fillRect(_dx - (RADIUS >> 1),
                _dy - (RADIUS >> 1),
                RADIUS,
                RADIUS);
    }

}
