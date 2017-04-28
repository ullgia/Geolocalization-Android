package com.example.giacomo.venicewifi;

import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.text.Html.fromHtml;

public class GetPolyFromJSON {
    public ArrayList getRoutes(){
        return routes;
    }
    public GetPolyFromJSON() {
    }
    ArrayList routes;
    String durata="";
    String lunghezza="";
    public String getDurata(){
        return durata;
    }
    public String getLunghezza(){
        return lunghezza;
    }
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
        routes = new ArrayList();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        JSONObject jDuration=null;
        JSONObject jLenght = null;
        String fragmentMode="";
        String travelModefromJson ="";
        try {
            jRoutes = jObject.getJSONArray("routes");

            for(int e = 0; e < jRoutes.length(); ++e) {
                jLegs = ((JSONObject)jRoutes.get(e)).getJSONArray("legs");
                ArrayList path = new ArrayList();


                for(int j = 0; j < jLegs.length(); ++j) {
                    jDuration = ((JSONObject)jLegs.get(j)).getJSONObject("duration");
                    durata = jDuration.getString("text");
                    jLenght =  ((JSONObject)jLegs.get(j)).getJSONObject("distance");
                    lunghezza = jLenght.getString("text");
                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");

                    for(int k = 0; k < jSteps.length(); ++k) {
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        fragmentMode =fromHtml((String)(((JSONObject)jSteps.get(k)).get("html_instructions"))).toString();
                        travelModefromJson  = ((String)(((JSONObject)jSteps.get(k)).get("travel_mode")));
                        List list = this.decodePoly(polyline);

                        for(int l = 0; l < list.size(); ++l) {
                            HashMap hm = new HashMap();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude));
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude));
                            hm.put("travel", fragmentMode);
                            hm.put("travelMode",travelModefromJson);
                           path.add(hm);
                        }
                    }

                    routes.add(path);
                }
            }
        } catch (JSONException var14) {
            var14.printStackTrace();
        } catch (Exception var15) {
            ;
        }

        return routes;
    }

    private List<LatLng> decodePoly(String encoded) {
        ArrayList poly = new ArrayList();
        int index = 0;
        int len = encoded.length();
        int lat = 0;
        int lng = 0;

        while(index < len) {
            int shift = 0;
            int result = 0;

            int b;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 31) << shift;
                shift += 5;
            } while(b >= 32);

            int dlat = (result & 1) != 0?~(result >> 1):result >> 1;
            lat += dlat;
            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 31) << shift;
                shift += 5;
            } while(b >= 32);

            int dlng = (result & 1) != 0?~(result >> 1):result >> 1;
            lng += dlng;
            LatLng p = new LatLng((double)lat / 100000.0D, (double)lng / 100000.0D);
            poly.add(p);
        }

        return poly;
    }
}

