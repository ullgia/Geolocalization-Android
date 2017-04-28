package com.example.giacomo.venicewifi;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class DownloadUrl {
    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL e = new URL(strUrl);
            urlConnection = (HttpURLConnection)e.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";

            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();
        } catch (Exception var12) {
            Log.d("Exception downloading", var12.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }
}
