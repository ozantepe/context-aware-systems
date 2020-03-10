package com.component.gis;

import com.database.server.IGeoServer;
import com.dto.PositionPOI;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;

public class GISController {

    private GISModel gisModel;

    private Point2D.Double zoomStartPoint;
    private Point2D.Double dragStartPoint;
    public static final double ZOOM_SCALE = 1.3;

    GISController(GISModel gisModel, IGeoServer geoServer) {
        this.gisModel = gisModel;
        this.gisModel.setGeoServer(geoServer);
    }

    void loadData() {
        gisModel.loadData();
        gisModel.repaint();
    }

    void zoomToFit() {
        gisModel.zoomToFit();
        gisModel.repaint();
    }

    void zoom(double factor) {
        gisModel.zoom(factor);
        gisModel.repaint();
    }

    void canvasPaneSizeChanged(ObservableValue<? extends Number> observable) {
        if (observable instanceof ReadOnlyDoubleProperty) {
            ReadOnlyDoubleProperty doubleProperty = (ReadOnlyDoubleProperty) observable;
            double val = doubleProperty.doubleValue();
            String name = doubleProperty.getName();
            if (name.equalsIgnoreCase("width")) {
                gisModel.setWidth(val);
            } else if (name.equalsIgnoreCase("height")) {
                gisModel.setHeight(val);
            }
        }
    }

    void drag(int x, int y) {
        gisModel.drag(x, y);
        gisModel.repaint();
    }

    void updatePOI(Object data) {
        gisModel.setDataToShow(new ArrayList<>(gisModel.getOriginalData()));
        Set<Integer> typeIds = (Set<Integer>) data;
        if (!typeIds.isEmpty()) {
            gisModel.getDataToShow().removeIf(geoObject -> !typeIds.contains(geoObject.getType()));
        }
        gisModel.repaint();
    }

    void updateGPS(Object data) {
        PositionPOI positionPOI = (PositionPOI) data;
        gisModel.getDataToShow().add(positionPOI);
        gisModel.repaint();
    }

    void mousePressed(MouseEvent event) {
        zoomStartPoint = new java.awt.geom.Point2D.Double(event.getX(), event.getY());
        dragStartPoint = new java.awt.geom.Point2D.Double(event.getX(), event.getY());
    }

    void mouseReleased(MouseEvent event) {
        MouseButton button = event.getButton();
        switch (button) {
            case PRIMARY: {
                // zoom gesture finished
                java.awt.geom.Rectangle2D rect = new java.awt.geom.Rectangle2D.Double(zoomStartPoint.getX(), zoomStartPoint.getY(), 0, 0);
                java.awt.geom.Point2D point = new java.awt.geom.Point2D.Double(event.getX(), event.getY());
                rect.add(point);
                gisModel.zoom(rect);
                gisModel.repaint();
                break;
            }
            case SECONDARY: {
                // drag gesture finished
                double dX = event.getX() - zoomStartPoint.getX();
                double dY = event.getY() - zoomStartPoint.getY();
                gisModel.drag((int) dX, (int) dY);
                gisModel.repaint();
                break;
            }
            default:
                System.out.println("Unhandled mouse button encountered: " + button.name());
                break;
        }
    }

    void mouseDragged(GISView gisView, MouseEvent event) {
        MouseButton button = event.getButton();
        switch (button) {
            case PRIMARY: {
                // zoom rect gesture
                java.awt.geom.Rectangle2D rect = new java.awt.geom.Rectangle2D.Double(zoomStartPoint.getX(), zoomStartPoint.getY(), 0, 0);
                java.awt.geom.Point2D point = new java.awt.geom.Point2D.Double(event.getX(), event.getY());
                rect.add(point);
                gisView.drawXOR(new Rectangle2D(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
                break;
            }
            case SECONDARY: {
                // drag gesture
                java.awt.geom.Point2D.Double point = new java.awt.geom.Point2D.Double(event.getX(), event.getY());
                double dX = point.getX() - dragStartPoint.getX();
                double dY = point.getY() - dragStartPoint.getY();
                gisView.translate(dX, dY);
                dragStartPoint = point;
                break;
            }
            default:
                System.out.println("Unhandled mouse button encountered: " + button.name());
                break;
        }

    }

    void scroll(ScrollEvent event) {
        double delta = event.getDeltaY();
        double scale;
        if (delta >= 0) {
            scale = 1 / ZOOM_SCALE;
        } else {
            scale = ZOOM_SCALE;
        }
        gisModel.zoom(new Point((int) event.getX(), (int) event.getY()), scale);
        gisModel.repaint();
    }
}
