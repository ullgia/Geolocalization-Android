package com.example.giacomo.venicewifi;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Giacomo on 30/11/2016.
 */

public class GetRoute {
    Activity mActivity;
    LatLng hotSpot;
    String travelMode;
    public GetRoute(Activity activity, LatLng marker, String travel){
        super();
        mActivity = activity;
        hotSpot = marker;
        travelMode = travel;
    }
    public void getRoute() {
        SimpleLocation location = new SimpleLocation(mActivity);
        final LatLng origin = new LatLng(location.getLatitude(), location.getLongitude());
        final LatLng dest =hotSpot;
        final GetDirectionsUrl getUrl = new GetDirectionsUrl();
        final String url = getUrl.getDirectionsUrl(origin, dest, travelMode);
        DownloadTask downloadTask = new DownloadTask(mActivity);
        downloadTask.execute(new String[]{url});
    }
}
