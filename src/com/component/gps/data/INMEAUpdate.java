package com.component.gps.data;

/**
 * Interface that informs interested objects about
 * the existence of a new NMEA-0183 data block
 *
 * @author jkroesche
 */
public interface INMEAUpdate {

    /**
     * A new NMEA-0183 data block is available
     *
     * @param _data the new data block
     */
    public void update(NMEAInfo _data);
}
