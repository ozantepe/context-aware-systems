package com;

public interface IMediator {

    void registerGISComponent(GISComponent gisComponent);

    void registerGPSComponent(GPSComponent gpsComponent);

    void registerPOIComponent(POIComponent poiComponent);
}
