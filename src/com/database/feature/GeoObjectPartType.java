package com.database.feature;

public enum GeoObjectPartType {
    HOLE,
    MAIN,
    EXCLAVE,
    UNDEFINED;

    public String toString() {
        switch (this) {
            case HOLE:
                return "HOLE";
            case MAIN:
                return "MAIN";
            case EXCLAVE:
                return "EXCLAVE";
            default:
                return "UNDEFINED";
        } // switch type
    }
}
