package com.dto;

public enum PoiType {

    POLICE(10003),
    BANK(10007),
    PARKING(10011);

    private Integer typeId;

    PoiType(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getTypeId() {
        return typeId;
    }
}
