package com.example.giacomo.venicewifi;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class GetOriginPoint {
    Activity mActivity;
    boolean gps;
    SimpleLocation mlocation;
    LatLng cityLatLon;
    public GetOriginPoint(Activity activity, boolean gpsIsOn, SimpleLocation location) {
        super();
        mActivity = activity;
        gps = gpsIsOn;
        mlocation = location;
    }
    public void getLatLonCity(String city){
        switch (city){
            case "rome":
                cityLatLon = new LatLng(41.890251, 12.492373);
                break;
            case "venice":
                cityLatLon = new LatLng(45.438611, 12.326667);
                break;
            case "milan":
                cityLatLon = new LatLng(45.464211, 9.191383);
                break;
            case "genova":
                cityLatLon = new LatLng( 44.414165, 8.942184);
                break;
            case "lecce":
                cityLatLon = new LatLng(40.35481, 18.17244);
                break;
            case "perugia":
                cityLatLon = new LatLng( 42.923748, 12.392921);
                break;
            case "reggio":
                cityLatLon = new LatLng(44.69825, 10.63125);
                break;
            case "bologna":
                cityLatLon = new LatLng(44.4833333, 11.3333333);
                break;

        }
    }
    public void getOriginPointMarker(String city) {
        if (gps) {
            if (ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                ActivityMaps.mMap.setMyLocationEnabled(true);
            getLatLonCity(city);
            ActivityMaps.mMap.moveCamera(CameraUpdateFactory.newLatLng(cityLatLon));
            ActivityMaps.mMap.moveCamera(CameraUpdateFactory.zoomTo(10));

        }
    }
}
