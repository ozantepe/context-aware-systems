package com.dto;

public enum MessageType {

    FROM_POI("Sending POI objects from POI component to GIS component"),
    FROM_GPS("Sending NMEAInfo objects from GPS component to GIS component"),
    FROM_AAL("Sending ContextElement objects from AAL component to GIS component"),
    FROM_CM("Sending ContextSituation objects from CM component");

    private String message;

    MessageType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
