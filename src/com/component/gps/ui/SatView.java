package com.component.gps.ui;

import com.component.gps.data.ASatelliteInfo;
import com.component.gps.data.INMEAUpdate;
import com.component.gps.data.NMEAInfo;
import javafx.application.Platform;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class SatView extends Pane implements INMEAUpdate {

    /// internal id to access canvas
    private static final String CANVAS_ID = "satView-canvas";
    /// internal constant for the padding of the crosshair
    private static final int PADDING = 20;

    /**
     * Initialize the layout of the satellite panel ...
     */
    public void init() {
        setStyle("-fx-background-color:lightgrey");

        Canvas canvas = new Canvas();

        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        canvas.setId(CANVAS_ID);

        this.getChildren().add(canvas);
        this.setMinSize(0, 0);
    }

    @Override
    public void update(NMEAInfo _data) {
        if (_data != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    drawSatellite(_data.getSats());
                }
            });
        }
    }

    /**
     * Draws the satellites on the internal canvas
     *
     * @param _sats list of satellites to be drawn
     */
    protected void drawSatellite(List<ASatelliteInfo> _sats) {
        Canvas c = (Canvas) lookup("#" + CANVAS_ID);
        GraphicsContext gc = c.getGraphicsContext2D();
//		System.out.println("CANVAS width  --> " + c.getWidth());
//		System.out.println("CANVAS height --> " + c.getHeight());

        int widthCanvas = (int) (c.getWidth());
        int heightCanvas = (int) (c.getHeight());

        gc.clearRect(0, 0, widthCanvas, heightCanvas);

        int centerX = widthCanvas >> 1;
        int centerY = heightCanvas >> 1;

        int diameter = (widthCanvas > heightCanvas) ? heightCanvas : widthCanvas;
        diameter = diameter - (PADDING << 1);
        int radius = diameter >> 1;

        int diameter45 = (int) (Math.cos(Math.toRadians(45)) * diameter);

        //
        // draw crosshairs
        //
        gc.setStroke(Color.BLACK);
        // 1. circles (0� and 45�)
        gc.strokeOval(centerX - radius,
                centerY - radius,
                diameter,
                diameter);
        gc.strokeOval(centerX - (diameter45 >> 1),
                centerY - (diameter45 >> 1),
                diameter45,
                diameter45);
        // 2. Lines
        gc.strokeLine(centerX - (radius + (PADDING >> 1)), centerY, centerX + (radius + (PADDING >> 1)), centerY);
        gc.strokeLine(centerX, centerY - (radius + (PADDING >> 1)), centerX, centerY + (radius + (PADDING >> 1)));
        // 3. Text
        gc.setTextBaseline(VPos.BOTTOM);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.strokeText("E", centerX - (radius + (PADDING >> 1)), centerY);
        gc.strokeText("W", centerX + (radius + (PADDING >> 2)), centerY);
        gc.strokeText("N", centerX + (PADDING >> 3), centerY - (radius));
        gc.strokeText("S", centerX + (PADDING >> 3), centerY + (radius + (PADDING >> 1) + 5));

        //
        // draw satellites
        //
        for (ASatelliteInfo sat : _sats) {
            sat.draw(gc, centerX, centerY, radius);
        }
    }
}
