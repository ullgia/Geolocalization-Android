package com.example.giacomo.venicewifi;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class AskForPermissions {
    public boolean askForPermission(String permission, Integer requestCode, Activity thisActivity) {
        if (ContextCompat.checkSelfPermission(thisActivity, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(thisActivity, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(thisActivity, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(thisActivity, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
        if(ContextCompat.checkSelfPermission(thisActivity, permission) == PackageManager.PERMISSION_GRANTED)
            return true;
        else return false;
    }
}
