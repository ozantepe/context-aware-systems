package com.component.gis;

import com.database.feature.GeoObject;
import com.database.server.IGeoServer;
import com.database.utilities.DrawingContext;
import com.database.utilities.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class GISModel {

    private int width = 200;
    private int height = 200;

    private IDataObserver observer;
    private IGeoServer geoServer;
    private List<GeoObject> originalData;
    private List<GeoObject> dataToShow;
    private BufferedImage canvas;
    private Matrix matrix;

    public List<GeoObject> getOriginalData() {
        return originalData;
    }

    public List<GeoObject> getDataToShow() {
        return dataToShow;
    }

    public void setDataToShow(List<GeoObject> dataToShow) {
        this.dataToShow = dataToShow;
    }

    void setWidth(double width) {
        this.width = (int) width;
        canvas = null; // window size changed ... invalidate canvas
    }

    void setHeight(double height) {
        this.height = (int) height;
        canvas = null; // window size changed ... invalidate canvas
    }

    public void addListener(IDataObserver observer) {
        this.observer = observer;
    }

    void setGeoServer(IGeoServer geoServer) {
        this.geoServer = geoServer;
    }

    void loadData() {
        this.geoServer.connect(this.geoServer.getConn(), this.geoServer.getUser(), this.geoServer.getPass());
        originalData = this.geoServer.loadData();
        dataToShow = originalData;
    }

    BufferedImage initCanvas(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    void repaint() {
        if (canvas == null) {
            canvas = initCanvas(width, height);
        }
        Graphics g = canvas.getGraphics();

        // Clean up canvas
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, width, height);

        // Draw map
        g.setColor(Color.BLACK);
        for (GeoObject geoObject : dataToShow) {
            if (matrix != null) {
                DrawingContext.drawObject(geoObject, g, matrix);
            }
        }
        update(canvas);
    }

    private void update(BufferedImage canvas) {
        observer.update(canvas);
    }

    void zoomToFit() {
        Rectangle map = getMapBounds(dataToShow);
        Rectangle win = new Rectangle(0, 0, width, height);
        matrix = Matrix.zoomToFit(map, win);
    }

    Rectangle getMapBounds(List<GeoObject> data) {
        if (data != null && !data.isEmpty()) {
            Rectangle rect = data.get(0).getBounds();
            for (GeoObject geoObject : data) {
                rect = rect.union(geoObject.getBounds());
            }
            return rect;
        }
        return null;
    }

    void zoom(double factor) {
        int centerX = canvas.getWidth(null) / 2;
        int centerY = canvas.getHeight(null) / 2;
        zoom(new Point(centerX, centerY), factor);
    }

    void zoom(Point point, double factor) {
        matrix = Matrix.zoomToPoint(matrix, point, factor);
    }

    void drag(int x, int y) {
        matrix = Matrix.drag(matrix, x, y);
    }
}
