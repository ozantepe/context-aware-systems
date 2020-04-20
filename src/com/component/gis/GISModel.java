package com.component.gis;

import com.component.gis.warnings.IWarning;
import com.database.feature.GeoObject;
import com.database.server.IGeoServer;
import com.database.utilities.DrawingContext;
import com.database.utilities.Matrix;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GISModel {

  private int width = 200;
  private int height = 200;

  private IDataObserver observer;
  private IGeoServer geoServer;
  private List<GeoObject> geoObjects;
  private BufferedImage canvas;
  private Matrix matrix;

  private GeoObject userPosition;

  public Map<String, IWarning> warnings = new HashMap<>();

  public List<GeoObject> getGeoObjects() {
    return geoObjects;
  }

  public void setUserPosition(GeoObject userPosition) {
    this.userPosition = userPosition;
  }

  public Map<String, IWarning> getWarnings() {
    return warnings;
  }

  void setWidth(double width) {
    this.width = (int) width;
    canvas = null;
  }

  void setHeight(double height) {
    this.height = (int) height;
    canvas = null;
  }

  public void addListener(IDataObserver observer) {
    this.observer = observer;
  }

  void setGeoServer(IGeoServer geoServer) {
    this.geoServer = geoServer;
  }

  void loadData() {
    boolean isConnectionSuccessful =
            this.geoServer.connect(
                    this.geoServer.getConn(), this.geoServer.getUser(), this.geoServer.getPass());
    if (isConnectionSuccessful) {
      System.out.println("Connection to database is successful...");
      geoObjects = this.geoServer.loadData();
    } else {
      System.out.println("Connection to database is failed...");
    }
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
    for (GeoObject geoObject : geoObjects) {
      if (matrix != null) {
        DrawingContext.drawObject(geoObject, g, matrix);
      }
    }

    // Draw current user position
    if (userPosition != null && matrix != null) {
      DrawingContext.drawObject(userPosition, g, matrix);
    }

    // Draw warning images
    int yPos = 10;
    for (IWarning warning : warnings.values()) {
      g.drawImage(warning.getImage(), 10, yPos, null);
      yPos += warning.getImage().getHeight(null);
      yPos += 10;
    }

    update(canvas);
  }

  private void update(BufferedImage canvas) {
    observer.update(canvas);
  }

  void zoomToFit() {
    Rectangle map = getMapBounds(geoObjects);
    Rectangle window = new Rectangle(0, 0, width, height);
    matrix = Matrix.zoomToFit(map, window);
  }

  protected void zoomToFit(Rectangle world) {
    Rectangle window = new Rectangle(0, 0, width, height);
    matrix = Matrix.zoomToFit(world, window);
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

  void zoom(Rectangle2D _rect) {
    Rectangle windowRect = _rect.getBounds();
    Rectangle worldRect = matrix.invers().multiply(windowRect);
    zoomToFit(worldRect);
  }

  void zoom(Point point, double factor) {
    matrix = Matrix.zoomToPoint(matrix, point, factor);
  }

  void drag(int x, int y) {
    matrix = Matrix.drag(matrix, x, y);
  }
}
