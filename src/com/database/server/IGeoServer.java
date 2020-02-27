package com.database.server;


import com.database.feature.GeoObject;

import java.util.List;

public interface IGeoServer {

    String getConn();

    String getUser();

    String getPass();

    boolean connect(String _con, String _user, String _pass);

    List<GeoObject> loadData();
}
