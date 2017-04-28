package com.example.giacomo.venicewifi;

/**
 * Created by Giacomo on 13/12/2016.
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

import static com.example.giacomo.venicewifi.ActivityMaps.hotspotList;
import static com.example.giacomo.venicewifi.ActivityMaps.mMap;
import static com.example.giacomo.venicewifi.ActivityMaps.milan;
import static com.google.android.gms.wearable.DataMap.TAG;

/**
 * Created by Giacomo on 13/12/2016.
 */

public class GetHotspotMilan extends AsyncTask<Void, Void, Void> {

    Activity mActivity;
    String url = "https://opendata.publicwifi.it/api/v1/layers/wifimetropolitano/nodes.geojson?format=json";
    ClusterMaker myCluster = new ClusterMaker(hotspotList);
    public GetHotspotMilan(Activity activity){
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
        HttpHandler sh = new HttpHandler();
//            ReadOffilineFile test = new ReadOffilineFile();
        // Making a request to url and getting response
        String jsonStr = "";
        Log.e(TAG, "Response from url: " + jsonStr);
        if  (1==1) {
            LoadData load = new LoadData();
            jsonStr = load.loadData("HotspotMilano.txt",mActivity);
        }
        if (milan) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONObject d = jsonObj.getJSONObject("d");
                // Getting JSON Array node
                JSONArray features = d.getJSONArray("results");

                // looping through All features
                for (int i = 0; i < features.length(); i++) {
                    JSONObject c = features.getJSONObject(i);

                    // Geometry node is JSON Object
                    String lon =  c.getString("Clatitudine_839754201");
                    String lat =  c.getString("Clongitudin_36623077");

                    // Properties node is JSON Object
                    String codice = c.getString("Csito_3530577");
                    String descrizione = c.getString("Czona_3744680");
                    String stato = c.getString("Cgid_102338");


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
        milan = false;
        myCluster.create();
    }


}



