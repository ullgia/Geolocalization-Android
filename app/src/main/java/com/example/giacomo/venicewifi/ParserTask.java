package com.example.giacomo.venicewifi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    Activity mActivity;
    public ParserTask(Activity activity){
        super();
        mActivity = activity;
    }
    String linguaTempo="";
    String linguaDistanza="";

    String durata="";
    String lunghezza="";
    public String loadDataDefautl(String stringa){
        SharedPreferences sharedPref= mActivity.getSharedPreferences("mypref", 0);
        return  sharedPref.getString(stringa, "");
    }
    public void setTitleActivity(){
        String lingua = loadDataDefautl("lingua");
        switch (lingua){
            case "inglese":
                linguaTempo = "  Estimated Time ";
                linguaDistanza="\n  Distance ";
                break;
            case "italiano":
                linguaTempo= "  Tempo Stimato ";
                linguaDistanza = "\n  Distanza ";
                break;
            case "spagnolo":
                linguaTempo= "  Tiempo estimado ";
                linguaDistanza = "\n  Distancia ";
                break;
            case "portoghese":
                linguaTempo= "  Tiempo estimado ";
                linguaDistanza = "\n  Distância ";
                break;


        }
    }

    public List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        List routes = null;

        try {
            JSONObject jObject = new JSONObject(jsonData[0]);
            ActivityMaps.jString = jObject.toString();
            GetPolyFromJSON e = new GetPolyFromJSON();
            routes = e.parse(jObject);
            durata=e.getDurata();
            lunghezza=e.getLunghezza();
            ArrayList directions = e.getRoutes();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return routes;
    }

    public void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList points = null;
        PolylineOptions lineOptions = null;
        new MarkerOptions();
        TextView text1 = (TextView) mActivity.findViewById(R.id.duratioText);
        TextView text2 = (TextView) mActivity.findViewById(R.id.lenghtText);
        text1.setText("");
        text2.setText("");
        String travel="";
        String mode="";
        for(int i = 0; i < result.size(); ++i) {
            points = new ArrayList();
            lineOptions = new PolylineOptions();
            List path = (List)result.get(i);
            if(path!=null){
                for(int j = 0; j < path.size(); ++j) {
                    HashMap point = (HashMap) path.get(j);
                    double lat = Double.parseDouble((String) point.get("lat"));
                    double lng = Double.parseDouble((String) point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    travel = (String) point.get("travel");
                    mode = (String) point.get("travelMode");
                    points.add(position);
                }
                lineOptions.color(Color.BLUE);
                lineOptions.addAll(points);
                lineOptions.width(10.0F);






            }

            ActivityMaps.polyline =  ActivityMaps.mMap.addPolyline(lineOptions);
        }
        if(durata != "") {
            text1.setText("  Estimated Time " + durata +  "\n  Distance " + lunghezza);
        }
        else {
            text1.setText("  No routes");
        }
    }}

