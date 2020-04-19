package com.component.gps.parser;

import com.component.gps.GPSSimulator;
import com.component.gps.data.*;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class NMEAParser implements Runnable {
  /// private reference to the internally used GPS simulator
  private GPSSimulator mGPSSim;
  /// private globally known info container, contains information of one NMEA-0183 data block
  private NMEAInfo mInfo = null;
  /// private globally known container of satellite ids used for the positioning (GSA data sets)
  private List<Integer> mRelevantSats = new Vector<Integer>();
  /// private list of components interested in receiving NMEA-0183 information
  private List<INMEAUpdate> mListeners = new Vector<INMEAUpdate>();
  /// private reference to the used thread
  private Thread mT;
  /// private flag to quit processing queue
  private boolean mRUN = true;

  /**
   * Constructor for the NMEA parser. All information provided is used to activate a GPS simulator.
   * The data of the simulator is parsed and its information is communicated through NMEAInfo
   * objects
   *
   * @param _file   The file to be used as input
   * @param _filter The filter stmt used to identify a new block
   * @param _delay  The amount of milliseconds the method blocks until a new data block is returned
   */
  public NMEAParser(String _file, String _filter, int _delay) {
    try {
      mGPSSim = new GPSSimulator(_file, _filter, _delay);
    } catch (IOException e) {
      e.printStackTrace();
    }
    mT = new Thread(this);
    mT.start();
  }

  /**
   * Attaches a new Listener to the NMEA parser. Listeners are provided with a new NMEA-0183 data
   * block worth of information (in form of a NMEAInfo object) as soon as it is fully available.
   *
   * @param _e A reference to an NMEAUpdate instance
   */
  public void addListener(INMEAUpdate _e) {
    mListeners.add(_e);
  }

  @Override
  public void run() {
    String line = null;
    try {
      while ((line = mGPSSim.readLine()) != null && mRUN) {
        String payload = line.substring(1, line.indexOf('*'));
        String checksum = line.substring(line.indexOf('*') + 1, line.length());
        if (checksum(payload, checksum)) {
          String header = payload.substring(0, payload.indexOf(','));
          String talkerId = header.substring(0, 2);
          String msgType = header.substring(2, header.length());
          String dataS = payload.substring(payload.indexOf(',') + 1, payload.length());
          String[] data = dataS.split(",", -1);
          switch (msgType) {
            case "GGA": {
              // found block start ... send the finished block
              update(mInfo);
              // make a new info container ...
              mInfo = new NMEAInfo();
              parseGGA(talkerId, data);
            }
            break;
            case "GSA": {
              parseGSA(talkerId, data);
            }
            break;
            case "GSV": {
              parseGSV(talkerId, data);
            }
            break;
            case "RMC":
            case "TXT":
            case "GLL":
            case "ZDA":
            case "VTG":
              // ignore
              break;
            default:
              System.out.println("undefined msg type (" + msgType + ") encountered");
          } // switch header
        } else { // if checksum ok
          System.out.println(
                  "NMEAParser::run() checksum error in line ("
                          + line
                          + ") for payload ["
                          + payload
                          + "] and checksum ["
                          + checksum
                          + "]");
        }
      } // while line
    } catch (Exception _e) {
      System.out.println("Exception in line --> " + line);
      _e.printStackTrace();
    }
  }

  /**
   * Checks if the XOR combined bytes of _data are corresponding to the provided HEX _checksum
   *
   * @param _data     A data block which is checked against a checksum
   * @param _checksum The checksum that should be used as prove-sample
   * @return true, if the calculated checksum of data is the same as the provided checksum
   */
  private boolean checksum(String _data, String _checksum) {
    int checksum = Integer.parseInt(_checksum, 16);
    byte[] payload = _data.getBytes();
    int val = 0;
    for (byte p : payload) {
      val ^= p; // val = val XOR p
    } // for bytes in payload
    return val == checksum;
  }

  /**
   * Method that parses GSV data sentences. Typically multiple lines with the same talker id are
   * available.
   *
   * @param _talkerId The id of the information provider (GPS, Galileo, GLONASS, ...)
   * @param _data     The data set of the GSV data line
   */
  private void parseGSV(String _talkerId, String[] _data) {
    // 0 --> number of GSV lines
    // 1 --> current number of GSV line
    // 2 --> number of satellite data sets
    // n + 4 --> satellite data set

    for (int i = 3; i < _data.length; i = i + 4) {
      if (_data.length > i + 2) { // sentence contains data
        String idS = _data[i];
        String vS = _data[i + 1];
        String hS = _data[i + 2];
        String snrS = "0";
        if (i + 3 < _data.length) {
          snrS = _data[i + 3];
        }
        int id = (idS.length() > 0) ? Integer.parseInt(idS) : 0;
        int v = (vS.length() > 0) ? Integer.parseInt(vS) : 0;
        int h = (hS.length() > 0) ? Integer.parseInt(hS) : 0;
        int snr = (snrS.length() > 0) ? Integer.parseInt(snrS) : 0;

        ASatelliteInfo satInfo = null;
        switch (_talkerId) {
          case "GP": {
            // GPS Satellite
            satInfo = new GPS_SatelliteInfo(id, v, h, snr);
          }
          break;
          case "GA": {
            // Galileo Satellite
            satInfo = new Galileo_SatelliteInfo(id, v, h, snr);
          }
          break;
          case "GL": {
            // GLONASS Satellite
            satInfo = new GLONASS_SatelliteInfo(id, v, h, snr);
          }
          break;
          case "GN": {
            // GNSS Satellite
            satInfo = new GNSS_SatelliteInfo(id, v, h, snr);
          }
          break;
          case "GB":
          case "BD": {
            // Baidou Satellite
            satInfo = new Baidou_SatelliteInfo(id, v, h, snr);
          }
          break;
          default:
            System.out.println(
                    "NMEAParser::parseGSV unhandled talker ID encountered: " + _talkerId);
        } // switch talkerId
        mInfo.addSatInfo(satInfo);
      } // if sentence contains data
    } // for i
  }

  /**
   * Method that parses GSA data sentences. Typically multiple lines with the same talker id are
   * available.
   *
   * @param _talkerId The id of the information provider (GPS, Galileo, GLONASS, ...)
   * @param _data     The data set of the GSA data line
   */
  private void parseGSA(String _talkerId, String[] _data) {
    // 0 --> selection mode
    // 1 --> mode
    // 2 --> id of 1st used sat
    // 3 --> id of 2nd used sat
    // 4 --> id of 3rd used sat
    // 5 --> ...
    // 14 --> PDOP
    // 15 --> HDOP
    // 17 --> VDOP

    for (int i = 2; i < 12; i++) {
      if (_data[i] != null && _data[i].length() > 0) {
        mRelevantSats.add(Integer.valueOf(_data[i]));
      }
    } // for i

    mInfo.setPDOP((_data[14] != null) ? Double.valueOf(_data[14]) : 0.0);
    mInfo.setHDOP((_data[15] != null) ? Double.valueOf(_data[15]) : 0.0);
    mInfo.setVDOP((_data[16] != null) ? Double.valueOf(_data[16]) : 0.0);
  }

  /**
   * Method that parses GGA data sentences.
   *
   * @param _talkerId The id of the information provider (GPS, Galileo, GLONASS, ...)
   * @param _data     The data set of the GSV data line
   */
  private void parseGGA(String _talkerId, String[] _data) {
    //  0 --> UTC time
    //  1 --> lat (deg min)
    //  2 --> N/S
    //  3 --> lon (deg min)
    //  4 --> W/E
    //  5 --> fix type
    //  6 --> number of used sats for position calculation
    //  7 --> HDOP
    //  8 --> elevation over goid
    //  9 --> unit elevation
    // 10 --> diff elipsoid and goid
    // 11 --> unit elevation diff
    // TIME 073457
    String tS = _data[0];
    if (tS != null && tS.length() > 0) {
      String hS = tS.substring(0, 2);
      String mS = tS.substring(2, 4);
      String sS = tS.substring(4, 6);
      mInfo.setTime(hS + ":" + mS + ":" + sS);
    }

    // LATITUDE
    String latS = _data[1];
    if (latS != null && latS.length() > 0) {
      String latDegS = latS.substring(0, 2);
      String latMinS = latS.substring(2, latS.length());
      double latDeg = Double.parseDouble(latDegS);
      double latMin = Double.parseDouble(latMinS);
      double lat = latDeg + (latMin / 60.0);
      if (_data[2].equals("S")) lat = -lat; // N vs. S --> South negative lat values
      mInfo.setLatitude(lat);
    } // if lat data

    // LONGITUDE
    String lonS = _data[3];
    if (lonS != null && lonS.length() > 0) {
      String lonDegS = lonS.substring(0, 3);
      String lonMinS = lonS.substring(3, lonS.length());
      double lonDeg = Double.parseDouble(lonDegS);
      double lonMin = Double.parseDouble(lonMinS);
      double lon = lonDeg + (lonMin / 60.0);
      if (_data[4].equals("W")) lon = -lon; // W vs. E --> West negative values
      mInfo.setLongitude(lon);
    }

    // FIX TYPE
    String fixS = (_data[5] != null && _data[5].length() > 0) ? _data[5] : "0";
    mInfo.setFix(Integer.parseInt(fixS));

    // AMOUNT OF SATS USED
    String satAmountS = (_data[6] != null && _data[6].length() > 0) ? _data[6] : "0";
    mInfo.setUsedSat(Integer.parseInt(satAmountS));

    // ELEVATION
    String elevGeoidS = (_data[8] != null && _data[8].length() > 0) ? _data[8] : "0.0";
    double elevGeoid = Double.parseDouble(elevGeoidS);
    String elevElipS = (_data[10] != null && _data[10].length() > 0) ? _data[10] : "0.0";
    double elevElip = Double.parseDouble(elevElipS);
    mInfo.setHeight(elevGeoid + elevElip);
  }

  private void update(NMEAInfo _data) {
    // set relevant Sats ...
    if (mRelevantSats != null) {
      for (int satId : mRelevantSats) {
        for (ASatelliteInfo sat : mInfo.getSats()) {
          if (sat.getID() == satId) {
            sat.setUsed();
            break;
          } // if found
        } // for sat info
      } // for mRelSats
    } // != null
    mRelevantSats.clear();
    for (INMEAUpdate listener : mListeners) {
      listener.update(_data);
    } // for mListeners
  }

  /**
   * Method that closes the thread and stops the GPS simulator
   */
  public void close() {
    mRUN = false;
    mT.interrupt();
  }
}
