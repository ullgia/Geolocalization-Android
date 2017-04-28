package com.example.giacomo.venicewifi;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class MyMarker implements ClusterItem {
    private final LatLng mPosition;
    public final int profilePhoto;
    public String name;
    public String stato;
    public MyMarker(Double lat,Double lon, String nome,String status, int pictureResource) {
        super();
        name = nome;
        profilePhoto = pictureResource;
        mPosition = new LatLng(lat,lon);
        stato= status;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
    public boolean isOK(){
        return (stato.contains("ok"));
    }
    public int getProfilePhoto(){
        return profilePhoto;
    }
}
