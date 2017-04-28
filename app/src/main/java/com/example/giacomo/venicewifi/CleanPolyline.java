package com.example.giacomo.venicewifi;

import android.app.Activity;
import android.widget.TextView;

import com.google.android.gms.maps.model.Polyline;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class CleanPolyline {
    Activity mActivity;
    Polyline mPolyline;
    public CleanPolyline(Activity activity, Polyline polyline){
        mActivity = activity;
        mPolyline = polyline;
    }
    public void cleanPoyline() {
        try {
            mPolyline.remove();
            TextView text1 = (TextView) mActivity.findViewById(R.id.lenghtText);
            TextView text2 = (TextView) mActivity.findViewById(R.id.duratioText);
            text1.setText("");
            text2.setText("");
        } catch (Exception e) {
        }
    }
}
