package com.database.utilities;

import com.database.feature.GeoObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class DrawingContext {

    protected Color backgroundColor = Color.lightGray;

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Set the color values for the geo objects based on their type rivers --> blue woods --> green
     * ... --> ...
     */
    public void drawObject(GeoObject _obj, Graphics _g, Matrix _matrix) {
        if (_obj != null && _g != null && _matrix != null) {
            switch (_obj.getType()) {
                case 1112: { // Bundeslaender
                    _obj.paint(_g, _matrix, new Color(211, 255, 190), new Color(255, 127, 127));
                }
                break;
                case 233: {
                    _obj.paint(_g, _matrix, Color.white, Color.black);
                }
                break;
                case 666: {
                    _obj.paint(_g, _matrix, Color.black, Color.black);
                }
                break;
                case 931: {
                    _obj.paint(_g, _matrix, Color.red, Color.black);
                }
                break;
                case 932: {
                    _obj.paint(_g, _matrix, Color.orange, Color.red);
                }
                break;
                case 933: {
                    _obj.paint(_g, _matrix, null, Color.black);
                }
                break;
                case 934: {
                    _obj.paint(_g, _matrix, null, Color.black);
                }
                break;
                case 1101: {
                    _obj.paint(_g, _matrix, Color.magenta, Color.green);
                }
                break;
                case 2001:
                case 2002:
                case 2003:
                case 2004:
                case 2005:
                case 2006:
                case 2007:
                case 4008: // leisure:swimming_pool
                case 4009: // leisure:water_park
                case 5013: // landuse:reservoir
                case 5015: // landuse:basin
                case 6005: // natural:water
                {
                    // OSM Waterway + alternative water features
                    Color f = Color.BLUE; // fill   color
                    Color b = f.darker(); // border color
                    _obj.paint(_g, _matrix, f, b);
                }
                break;
                //
                // Gr�nfl�chen ...
                //
                case 4001: // leisure:nature_reserve
                case 4002: // leisure:park
                case 5005: // landuse:grass
                case 5006: // landuse:meadow
                case 5008: // landuse:farmland
                case 5010: // landuse:allotments
                case 5012: // landuse:recreation_ground
                case 5017: // landuse:village_green
                case 5019: // landuse:crop
                case 6001: // natural:grassland
                case 6004: // natural:fell
                case 6012: // natural:wetland
                {
                    Color b = Color.GREEN;
                    Color f = b.brighter();
                    _obj.paint(_g, _matrix, f, b);
                }
                break;
                //
                // Waldfl�chen ...
                //
                case 5004: // landuse:forest
                case 5014: // landuse:vineyard
                case 6002: // natural:wood
                case 6003: // natural:scrub
                {
                    Color b = Color.GREEN;
                    Color f = b.darker();
                    _obj.paint(_g, _matrix, f, b);
                }
                break;
                //
                // Fl�chen ...
                //
                case 6006: // natural:land
                case 6008: // natural:beach
                case 7005: // place:island
                {
                    Color b = Color.black;
                    Color f = new Color(245, 245, 220);
                    _obj.paint(_g, _matrix, f, b);
                }
                break;
                //
                // bebaute Fl�chen ...
                //
                case 4003: // leisure:common
                case 5001: // landuse:residential
                case 5002: // landuse:industrial
                case 5003: // landuse:commercial
                case 5011: // landuse:cemetery
                case 5016: // landuse:retail
                case 6011: // natural:scree
                {
                    Color b = Color.GRAY;
                    Color f = b.brighter();
                    _obj.paint(_g, _matrix, f, b);
                }
                break;
                //
                // Geb�ude ...
                //
                case 9001: // building:
                case 9002:
                case 9003:
                case 9004:
                case 9005:
                case 9006:
                case 9007:
                case 9008:
                case 9009:
                case 9010:
        case 9011:
        case 9012:
        case 9013:
        case 9014:
        case 9015:
        case 9016:
        case 9017:
        case 9018:
        case 9019:
        case 9020:
        case 9021:
        case 9022:
        case 9023:
        case 9024:
        case 9025:
        case 9026:
        case 9027:
        case 9028:
        case 9099:
        case 10001: // amenity:
        case 10002:
        case 10004:
        case 10005:
        case 10006:
        case 10008:
        case 10009:
        case 10010:
        case 10012:
        case 10013:
        case 10014:
        case 10015:
                case 10016:
                case 10017:
                case 10018:
                case 10019:
                case 10020:
                case 10021:
                case 10022:
                case 10023:
                case 10024:
                case 10025:
                case 10026: {
                    Color f = new Color(139, 69, 19);
                    Color b = f.darker();
                    _obj.paint(_g, _matrix, f, b);
                }
                break;
                case 10027: {
                    _g.setColor(Color.RED);
                    Rectangle normalizedRect = _matrix.multiply(_obj.getBounds());
                    _g.drawString("USER", normalizedRect.x, normalizedRect.y);
                    _obj.paint(_g, _matrix, Color.RED, Color.RED);
                }
                break;
                case 10003: {
                    handleDrawingPOI(_obj, _g, _matrix, "icons/police.png", "POLICE");
                }
                break;
                case 10007: {
                    handleDrawingPOI(_obj, _g, _matrix, "icons/bank.png", "BANK");
                }
                break;
                case 10011: {
                    handleDrawingPOI(_obj, _g, _matrix, "icons/parking.png", "PARKING");
                }
                break;
                default: {
                    _obj.paint(_g, _matrix, null, Color.black);
                }
            } // switch
        } // if parameter null
    }

    private void handleDrawingPOI(
            GeoObject _obj, Graphics _g, Matrix _matrix, String imageFilePath, String title) {
        if (_obj.isActivePOI()) {
            drawImage(_g, imageFilePath, _matrix.multiply(_obj.getBounds()), title);
        } else {
            _obj.paint(_g, _matrix, null, Color.black);
        }
    }

    private void drawImage(Graphics _g, String filePath, Rectangle normalizedBounds, String title) {
        BufferedImage policeImage;
        try {
            policeImage = ImageIO.read(new File(filePath));
            _g.setColor(Color.BLUE);
            _g.drawString(title, normalizedBounds.x, normalizedBounds.y);
            _g.drawImage(
                    policeImage,
                    normalizedBounds.x - policeImage.getWidth(null) / 2,
                    normalizedBounds.y - policeImage.getHeight(null) / 2,
                    50,
                    50,
                    null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
