package com.component.gis;

import com.database.server.IGeoServer;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.Set;

public class GISController {

    private GISModel gisModel;

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

    void update(Object data) {
        gisModel.setDataToShow(new ArrayList<>(gisModel.getOriginalData()));

        Set<Integer> typeIds = (Set<Integer>) data;
        if (!typeIds.isEmpty()) {
            gisModel.getDataToShow().removeIf(geoObject -> !typeIds.contains(geoObject.getType()));
        }
        gisModel.repaint();
    }
}
