package com.database.server;

import com.database.feature.*;
import org.postgis.Geometry;
import org.postgis.LinearRing;
import org.postgis.PGgeometry;
import org.postgresql.PGConnection;
import org.postgresql.util.PGobject;

import java.awt.*;
import java.sql.*;
import java.util.List;
import java.util.Vector;

public class OSMGeoServer implements IGeoServer {
  //		ResultSet r = s.executeQuery("SELECT * FROM amenity_austria_points AS a WHERE a.geom &&
  // ST_MakeEnvelope(14.3815, 48.2054, 14.3817, 48.2056)");

  // lb java.awt.Point[x=14084774,y=48132875]
  // rt java.awt.Point[x=14540614,y=48448764]

  private Connection mConn;

  private static final int OFFSET = 1000000;

  public static final String[] mQueries = {
          "SELECT * FROM osm_boundary AS a",
          "SELECT * FROM osm_landuse AS a",
          "SELECT * FROM osm_natural AS a",
          "SELECT * FROM osm_place AS a",
          "SELECT * FROM osm_amenity AS a",
          "SELECT * FROM osm_leisure AS a",
          "SELECT * FROM osm_waterway AS a",
          "SELECT * FROM osm_building AS a",
          "SELECT * FROM osm_highway AS a",
          "SELECT * FROM osm_railway AS a" /**/
  };

  private static String BBOX =
          "WHERE a.geom && ST_MakeEnvelope(14.084774, 48.132875, 14.540614, 48.448764)";

  public static final String USER = "geo";
  public static final String PASS = "geo";
  public static final String CONN = "jdbc:postgresql://localhost:5432/osm";

  /**
   * @param _user geo
   * @param _pass geo
   * @param _con  jdbc:postgresql://localhost:5432/OSM_Austria
   * @return true, if connection established
   */
  public boolean connect(String _con, String _user, String _pass) {
    try {
      Class.forName("org.postgis.DriverWrapper");
      mConn = DriverManager.getConnection(_con, _user, _pass);
      /* Add the geometry types to the connection. */
      PGConnection c = (PGConnection) mConn;
      c.addDataType(
              "geometry", (Class<? extends PGobject>) Class.forName("org.postgis.PGgeometry"));
      c.addDataType("box2d", (Class<? extends PGobject>) Class.forName("org.postgis.PGbox2d"));
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public List<GeoObject> loadData() {
    return getData(mQueries);
  }

  public List<GeoObject> getData(String[] _queries) {
    System.out.println("loading data ... ");
    Vector<GeoObject> data = new Vector<>();
    Statement s;
    try {
      s = mConn.createStatement();
    } catch (SQLException e1) {
      e1.printStackTrace();
      return null;
    }

    for (String query : _queries) {
      try {
        ResultSet r = s.executeQuery(query);
        while (r.next()) {
          try {
            String id = r.getString("id");
            int type = r.getInt("type");
            GeoObject obj = new GeoObject(id, type);
            PGgeometry geom = (PGgeometry) r.getObject("geom");
            switch (geom.getGeoType()) {
              case Geometry.POINT: {
                //			    	System.out.println("found object ("+ id + ") Point ... ");
                Geometry g = geom.getGeometry();
                org.postgis.Point pPG = g.getPoint(0);
                Point pt = new Point((int) (pPG.x * OFFSET), (int) (pPG.y * OFFSET));
                AGeoObjectPart part = new GeoObjectPart_Point(pt, GeoObjectPartType.MAIN);
                obj.addPart(part);
              }
              break;
              case Geometry.LINESTRING: {
                //			    	System.out.println("found object ("+ id + ") Line ... ");
                Geometry g = geom.getGeometry();
                Point[] pts = new Point[g.numPoints()];
                for (int i = 0; i < g.numPoints(); i++) {
                  org.postgis.Point pPG = g.getPoint(i);
                  pts[i] = new Point((int) (pPG.x * OFFSET), (int) (pPG.y * OFFSET));
                }
                AGeoObjectPart part = new GeoObjectPart_Linestring(pts, GeoObjectPartType.MAIN);
                obj.addPart(part);
              }
              break;
              case Geometry.POLYGON: {
                //			    	System.out.println("found object ("+ id + ") Polygon ... ");
                String wkt = geom.toString();
                org.postgis.Polygon p = new org.postgis.Polygon(wkt);
                if (p.numRings() > 1) {
                  Polygon poly = new Polygon();
                  // Ring 0 --> main polygon ... rest should be holes
                  LinearRing ring = p.getRing(0);
                  for (int i = 0; i < ring.numPoints(); i++) {
                    org.postgis.Point pPG = ring.getPoint(i);
                    poly.addPoint((int) (pPG.x * OFFSET), (int) (pPG.y * OFFSET));
                  }
                  AGeoObjectPart main = new GeoObjectPart_Area(poly, GeoObjectPartType.MAIN);
                  // extract the holes
                  for (int j = 1; j < p.numRings(); j++) {
                    ring = p.getRing(j);
                    poly = new Polygon();
                    for (int k = 0; k < ring.numPoints(); k++) {
                      org.postgis.Point pPG = ring.getPoint(k);
                      poly.addPoint((int) (pPG.x * OFFSET), (int) (pPG.y * OFFSET));
                    } // for points
                    AGeoObjectPart hole = new GeoObjectPart_Area(poly, GeoObjectPartType.HOLE);
                    main.addPart(hole);
                  } // for holes
                  obj.addPart(main);
                } else { // poly without holes
                  Polygon poly = new Polygon();
                  for (int i = 0; i < p.numPoints(); i++) {
                    org.postgis.Point pPG = p.getPoint(i);
                    poly.addPoint((int) (pPG.x * OFFSET), (int) (pPG.y * OFFSET));
                  }
                  AGeoObjectPart part = new GeoObjectPart_Area(poly, GeoObjectPartType.MAIN);
                  obj.addPart(part);
                } // poly with or without holes
              }
              break;
              case Geometry.MULTIPOLYGON: {
                //			    	System.out.println("found object ("+ id + ") MultiPolygon ... ");
                String wkt = geom.toString();
                org.postgis.MultiPolygon mp = new org.postgis.MultiPolygon(wkt);
                for (int i = 0; i < mp.getPolygons().length; i++) {
                  // iterate over polygon
                  org.postgis.Polygon p = mp.getPolygons()[i];
                  GeoObjectPartType partType;
                  if (i == 0) {
                    // first poly is primary ...
                    partType = GeoObjectPartType.MAIN;
                  } else {
                    // next  polys are exclaves ...
                    partType = GeoObjectPartType.EXCLAVE;
                  }
                  Polygon poly = new Polygon();
                  // first ring is exterior polygonal shape
                  LinearRing ring = p.getRing(0);
                  for (int j = 0; j < ring.numPoints(); j++) {
                    org.postgis.Point pPG = ring.getPoint(j);
                    poly.addPoint((int) (pPG.x * OFFSET), (int) (pPG.y * OFFSET));
                  }
                  AGeoObjectPart part = new GeoObjectPart_Area(poly, partType);
                  // next rings are holes
                  // extract the holes
                  for (int j = 1; j < p.numRings(); j++) {
                    ring = p.getRing(j);
                    poly = new Polygon();
                    for (int k = 0; k < ring.numPoints(); k++) {
                      org.postgis.Point pPG = ring.getPoint(k);
                      poly.addPoint((int) (pPG.x * OFFSET), (int) (pPG.y * OFFSET));
                    }
                    AGeoObjectPart hole = new GeoObjectPart_Area(poly, GeoObjectPartType.HOLE);
                    part.addPart(hole);
                  } // for all holes
                  obj.addPart(part);
                } // for all exclaves/parts
                data.add(obj);
              }
              break;
            } // switch geotype
            data.add(obj);
          } catch (Exception _e) {
            _e.printStackTrace();
          }
        } // while resultset
      } catch (SQLException _e) {
        _e.printStackTrace();
      }
    } // for query
    try {
      if (s != null) {
        s.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return data;
  }

  @Override
  public String getConn() {
    return CONN;
  }

  @Override
  public String getUser() {
    return USER;
  }

  @Override
  public String getPass() {
    return PASS;
  }

  public static void main(String[] _argv) {
    OSMGeoServer server = new OSMGeoServer();
    server.connect(OSMGeoServer.CONN, OSMGeoServer.USER, OSMGeoServer.PASS);
    server.getData(OSMGeoServer.mQueries);
  }
}
