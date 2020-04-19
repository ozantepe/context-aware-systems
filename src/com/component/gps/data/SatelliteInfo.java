package com.component.gps.data;

public class SatelliteInfo {
  private int mID = -1;
  private int mVertAngle = 0;
  private int mHoriAngle = 0;
  private int mSNR = 0;
  private boolean mUsed = false;

  public SatelliteInfo(int _id, int _v, int _h, int _s) {
    setID(_id);
    setVertAngle(_v);
    setHoriAngle(_h);
    setSNR(_s);
  }

  public boolean isUsed() {
    return mUsed;
  }

  public int getId() {
    return getID();
  }

  @Override
  public boolean equals(Object _other) {
    if (_other instanceof SatelliteInfo) {
      SatelliteInfo other = (SatelliteInfo) _other;
      return (getID() == other.getID()
              && getVertAngle() == other.getVertAngle()
              && getHoriAngle() == other.getHoriAngle()
              && getSNR() == other.getSNR());
    }
    return false;
  }

  @Override
  public String toString() {
    return "Satellite Info ("
            + getID()
            + ";"
            + getVertAngle()
            + ";"
            + getHoriAngle()
            + ";"
            + getSNR()
            + ")";
  }

  public int getVertAngle() {
    return mVertAngle;
  }

  public void setVertAngle(int _angle) {
    mVertAngle = _angle;
  }

  public int getHoriAngle() {
    return mHoriAngle;
  }

  public void setHoriAngle(int _angle) {
    mHoriAngle = _angle;
  }

  public int getID() {
    return mID;
  }

  public void setID(int _id) {
    mID = _id;
  }

  public int getSNR() {
    return mSNR;
  }

  public void setSNR(int _snr) {
    mSNR = _snr;
  }

  public void setUsed() {
    mUsed = true;
  }
}
