package com.example.giacomo.venicewifi;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Giacomo on 30/01/2017.
 */
public class WriteData {
    public void writeData(String inFile, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("defaultCity.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(inFile);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    }

