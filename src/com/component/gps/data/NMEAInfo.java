package com.component.gps.data;

import java.util.Vector;

public class NMEAInfo {
  private String mTime = null;
  private double mLatitude = 0.0;
  private double mLongitude = 0.0;
  private int mUsedSat = 0;
  private double mPDOP = 0.0;
  private double mHDOP = 0.0;
  private double mVDOP = 0.0;
  private int mFix = 0;
  private double mHeight = 0.0;

  private Vector<ASatelliteInfo> mSats = new Vector<ASatelliteInfo>();

  public void setTime(String _t) {
    mTime = _t;
  }

  public void setLatitude(double _l) {
    mLatitude = _l;
  }

  public void setLongitude(double _l) {
    mLongitude = _l;
  }

  public void setUsedSat(int _sat) {
    mUsedSat = _sat;
  }

  public void setPDOP(double _p) {
    mPDOP = _p;
  }

  public void setHDOP(double _h) {
    mHDOP = _h;
  }

  public void setVDOP(double _v) {
    mVDOP = _v;
  }

  public void setFix(int _f) {
    mFix = _f;
  }

  public void setHeight(double _height) {
    mHeight = _height;
  }

  public void setSats(Vector<ASatelliteInfo> _sats) {
    mSats = _sats;
  }

  public boolean addSatInfo(ASatelliteInfo _e) {
    if (!getSats().contains(_e)) {
      getSats().addElement(_e);
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuffer ret = new StringBuffer();
    ret.append("**** NMEA Info ****" + "\n");
    ret.append("time:      " + getTime() + "\n");
    ret.append("latitude:  " + mLatitude + "\n");
    ret.append("longitude: " + mLongitude + "\n");
    ret.append("-------------------------" + "\n");
    for (int i = 0; i < getSats().size(); i++) {
      ret.append((i + 1) + ". " + getSats().elementAt(i).toString() + "\n");
    } // for i
    ret.append("*******************" + "\n");
    return ret.toString();
  }

  public String getTime() {
    return mTime;
  }

  public int getFix() {
    return mFix;
  }

  public int getUsedSat() {
    return mUsedSat;
  }

  public double getPDOP() {
    return mPDOP;
  }

  public double getHDOP() {
    return mHDOP;
  }

  public double getVDOP() {
    return mVDOP;
  }

  public double getHeight() {
    return mHeight;
  }

  public Vector<ASatelliteInfo> getSats() {
    return mSats;
  }

  public double getLatitude() {
    return mLatitude;
  }

  public double getLongitude() {
    return mLongitude;
  }
}
