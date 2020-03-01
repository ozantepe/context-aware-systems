package com.component.gps.data;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Abstract base class for storing satellite related
 * NMEA-0183 data
 *
 * @author jkroesche
 */
public abstract class ASatelliteInfo {
	/// the NMEA-0183 id of the satellite
	private int mID = -1;
	/// the vertical angle (deg) of the satellite in relation to the position/time
	private int mVertAngle = 0;
	/// the horizontal angle (deg) of the satellite in relation to the position/time
	private int mHoriAngle = 0;
	/// the signal to noise ratio of the matching satellite
	private int mSNR = 0;
	/// flat if the satellite is used for the position calculation
	private boolean mUsed = false;

	/**
	 * Constructor for all satellite info classes
	 *
	 * @param _id     the id of the satellite
	 * @param _vAngle the vertical angle (deg) of the satellite
	 * @param _hAngle the horizontal angle (deg) of the satellite
	 * @param _snr    the signal to noise ratio of the satellite
	 */
	public ASatelliteInfo(int _id, int _vAngle, int _hAngle, int _snr) {
		setID(_id);
		setVertAngle(_vAngle);
		setHoriAngle(_hAngle);
		setSNR(_snr);
	}

	@Override
	public boolean equals(Object _other) {
		if (_other instanceof ASatelliteInfo) {
			ASatelliteInfo other = (ASatelliteInfo) _other;
			return (getID() == other.getID() &&
					getVertAngle() == other.getVertAngle() &&
					getHoriAngle() == other.getHoriAngle() &&
					getSNR() == other.getSNR());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Satellite Info (" + getID() + ";" + getVertAngle() + ";" + getHoriAngle() + ";" + getSNR() + ")";
	}

	/**
	 * returns the vertical angle of the satellite (in deg)
	 *
	 * @return the vertical angle (deg)
	 */
	public int getVertAngle() {
		return mVertAngle;
	}

	/**
	 * Sets the vertical angle of the satellite (in deg)
	 *
	 * @param _angle the angle in degree
	 */
	public void setVertAngle(int _angle) {
		mVertAngle = _angle;
	}

	/**
	 * returns the horizontal angle of the satellite (in deg)
	 *
	 * @return the horizontal angle (deg)
	 */
	public int getHoriAngle() {
		return mHoriAngle;
	}


	/**
	 * Sets the horizontal angle of the satellite (in deg)
	 *
	 * @param _angle the angle in degree
	 */
	public void setHoriAngle(int _angle) {
		mHoriAngle = _angle;
	}


	/**
	 * Returns the satellite id
	 *
	 * @return the satellite id
	 */
	public int getID() {
		return mID;
	}

	/**
	 * Sets the satellite id
	 *
	 * @param _id the id of the satellite
	 */
	public void setID(int _id) {
		mID = _id;
	}

	/**
	 * Returns the signal to noise ratio while receiving signals of this satellite
	 *
	 * @return the signal to noise ratio
	 */
	public int getSNR() {
		return mSNR;
	}

	/**
	 * Sets the signal to noise ratio of the satellite
	 *
	 * @param _snr the signal to noise ratio
	 */
	public void setSNR(int _snr) {
		mSNR = _snr;
	}

	/**
	 * Is the satellite used for the position calculation?
	 *
	 * @return true if used, else false
	 */
	public boolean isUsed() {
		return mUsed;
	}

	/**
	 * Sets if the satellite is used in the position calculation
	 */
	public void setUsed() {
		mUsed = true;
	}

	/**
	 * Initiates the drawing of the satellite into the satellite view
	 *
	 * @param _gc      graphics context used to draw the satellite
	 * @param _centerX x-value of the center of the sat-view
	 * @param _centerY y-value of the center of the sat-view
	 * @param _radius  radius of the sat-view
	 */
	public void draw(GraphicsContext _gc, int _centerX, int _centerY, int _radius) {
		int c = (int) (Math.cos(Math.toRadians(getVertAngle())) * _radius);

		int angle = getHoriAngle() + 90;

		int a = (int) (Math.sin(Math.toRadians(angle)) * c);
		int b = (int) (Math.cos(Math.toRadians(angle)) * c);

		int x = _centerX - b;
		int y = _centerY - a;

//		Interaction rectangle
//		Rectangle rect = new Rectangle(x - radius, 
//				                       y - radius, 
//				                       radius << 1, 
//				                       radius << 1);
//		mRects.addElement(rect);

		// draw surrounding marker
		Color clr;
		if (isUsed()) {
			clr = Color.GREEN.darker();
		} else if (getSNR() == 0) {
			clr = Color.RED;
		} else {
			double[] vals = new double[3];
			Color color = Color.rgb(242, 204, 132);
			vals[0] = color.getHue();
			vals[1] = color.getSaturation();
			vals[2] = 1 - (float) (getSNR() / 100.0f);
			clr = Color.hsb(vals[0], vals[1], vals[2]);
		}
		_gc.setFill(clr);
		drawSatellite(_gc, x, y);

		// RESET values
		_gc.setFill(Color.RED);
		_gc.setStroke(Color.BLACK);
	}

	/**
	 * Abstract method to finally draw the satellite (circle,
	 * rectangle, triangle, or alike) in an concrete class
	 *
	 * @param _gc Drawing Context used to draw the satellite
	 * @param _x  x-value of the satellite on the sat-view
	 * @param _y  y-value of the satellite on the sat-view
	 */
	protected abstract void drawSatellite(GraphicsContext _gc, int _x, int _y);

}
