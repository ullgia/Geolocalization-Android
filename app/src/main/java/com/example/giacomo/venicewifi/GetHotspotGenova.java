package com.example.giacomo.venicewifi;

/**
 * Created by Giacomo on 01/02/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.giacomo.venicewifi.ActivityMaps.genova;
import static com.example.giacomo.venicewifi.ActivityMaps.hotspotList;
import static com.example.giacomo.venicewifi.ActivityMaps.mMap;
import static com.google.android.gms.wearable.DataMap.TAG;

public class GetHotspotGenova extends AsyncTask<Void, Void, Void> {
    Activity mActivity;
    ClusterMaker myCluster = new ClusterMaker(hotspotList);
    public GetHotspotGenova(Activity activity){
        super();
        mActivity = activity;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        ActivityMaps.pDialog = new ProgressDialog(mActivity);
        ActivityMaps.pDialog.setMessage("Please wait...");
        ActivityMaps.pDialog.setCancelable(false);
        ActivityMaps.pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        String jsonStr = "";
        if  (jsonStr=="") {
            LoadData load = new LoadData();
            jsonStr = load.loadData("HotspotGenova.txt",mActivity);
        }
        if (genova) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray features = jsonObj.getJSONArray("features");

                // looping through All features
                for (int i = 0; i < features.length(); i++) {
                    JSONObject c = features.getJSONObject(i);

                    // Geometry node is JSON Object
                    JSONObject geometry = c.getJSONObject("geometry");
                    String lat =  geometry.getJSONArray("coordinates").getString(1);
                    String lon =  geometry.getJSONArray("coordinates").getString(0);

                    // Properties node is JSON Object
                    JSONObject properties = c.getJSONObject("properties");
                    String codice = properties.getString("ID");
                    String descrizione = properties.getString("NOME");
                    String stato = properties.getString("ANNO");


                    // tmp hash map for single contact
                    HashMap<String, String> hotspot = new HashMap<>();

                    // adding each child node to HashMap key => value
                    hotspot.put("codice", codice);
                    hotspot.put("descrizione",descrizione);
                    hotspot.put("lat",lat);
                    hotspot.put("lon",lon);
                    hotspot.put("stato",stato);

                    // adding contact to contact list
                    hotspotList.add(hotspot);
                }
            } catch (final JSONException e) {}

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Dismiss the progress dialog
        if (ActivityMaps.pDialog.isShowing())
            ActivityMaps.pDialog.dismiss();
        while(mMap == null){}
        genova= false;
        myCluster.create();
    }

}

