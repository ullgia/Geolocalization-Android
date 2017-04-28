package com.example.giacomo.venicewifi;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class LoadData {
    public String loadData(String inFile, Context context) {
        String tContents = "";

        try {
            InputStream stream = context.getAssets().open(inFile);

            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }

        return tContents;

    }

}
