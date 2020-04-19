package com.component.gps;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class that simulates a typical GPS receiver. It provides NMEA-0183 statements through its
 * readline() method. After each block (defined by a filter stmt) the readline() method actively
 * blocks for a specified sleep time. The data is provided based on a given file.
 *
 * @author jkroesche
 */
public class GPSSimulator extends BufferedReader {

	/// private filter string, used to identify information blocks
	private String mFilter;
	/// private sleep time, which is used after each data block
	private int mSleep;

	/**
	 * Constructor for the GPS simulator. It opens a file and provides its input line wise through the
	 * use of the readline() method. After each data block (identified through a filter stmt) the
	 * system actively blocks
	 *
	 * @param _file   The file to be used as input
	 * @param _filter The filter stmt used to identify a new block
	 * @param _sleep  The amount of milliseconds the method blocks until a new data block is returned
	 * @throws FileNotFoundException
	 */
	public GPSSimulator(String _file, String _filter, int _sleep) throws FileNotFoundException {
		super(new FileReader(_file));
		mFilter = _filter;
		mSleep = _sleep;
	}

	@Override
	public String readLine() throws IOException {
		String line = super.readLine();
		if (line != null && line.contains(mFilter)) {
			try {
				Thread.sleep(mSleep);
			} catch (InterruptedException _e) {
				_e.printStackTrace();
			}
		}
		return line;
	}
}
