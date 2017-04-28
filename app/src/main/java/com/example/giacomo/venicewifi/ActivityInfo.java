package com.example.giacomo.venicewifi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityInfo extends AppCompatActivity {

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }
    private String TAG = ActivityInfo.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://dati.venezia.it/sites/default/files/hotspots-wifi.json";

    ArrayList<HashMap<String, String>> directionList;
    String jSon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ActionBar infoActionBar = getSupportActionBar();
        infoActionBar.setTitle("Navigation Directions");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.activity_info);

        directionList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);
        Intent i = getIntent();
        jSon = i.getExtras().getString("directions");
        new GetDirectionsList().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    public class GetDirectionsList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ActivityInfo.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
          //  HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
          //  String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jSon);

            if (jSon != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jSon);

                    // Getting JSON Array node
                    JSONArray routes = jsonObj.getJSONArray("routes");

                    // looping through All features
                    for (int i = 0; i < routes.length(); i++) {
                        JSONArray legs = ((JSONObject)routes.get(i)).getJSONArray("legs");
                        for(int j = 0; j < legs.length(); ++j) {
                            JSONArray steps = ((JSONObject) legs.get(j)).getJSONArray("steps");
                            for(int k = 0; k < steps.length(); ++k) {
                                String fragmentLenght= (String)((JSONObject)((JSONObject)steps.get(k)).get("distance")).get("text");
                                String fragmentTime  = (String)((JSONObject)((JSONObject)steps.get(k)).get("duration")).get("text");
                                String fragmentHTML  = fromHtml((String)(((JSONObject)steps.get(k)).get("html_instructions"))).toString();
                                HashMap<String, String> directions = new HashMap<>();

                                // adding each child node to HashMap key => value
                                directions.put("html",fragmentHTML );
                                directions.put("time",fragmentTime);
                                directions.put("lenght",fragmentLenght);
                                // adding contact to contact list
                                 directionList.add(directions);
                    }
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ActivityInfo.this, directionList,
                    R.layout.list_item, new String[]{"html","lenght","time"}, new int[]{R.id.html
                    , R.id.lenght, R.id.time});

            lv.setAdapter(adapter);
        }

    }
}
