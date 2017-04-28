package com.example.giacomo.venicewifi;


import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import static com.example.giacomo.venicewifi.ActivityMaps.languageDefault;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class GetDirectionsUrl {
    String lingua="";
    public String getDirectionsUrl(LatLng origin, LatLng dest, String travelMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        getLingua();
        String travel = "mode=" + travelMode;
        String language="language=" + lingua;
        String parameters = str_origin + "&" + str_dest + "&" + travel+ "&" + language;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }
    public void getLingua(){
        switch (languageDefault){
            case "italiano":
                lingua= "it";
                break;
            case "inglese":
                lingua= "en";
                break;
            case "spagnolo":
                lingua= "es";
                break;
            case "portoghese":
                lingua= "pt";
                break;

        }

    }



}
