package com.example.giacomo.venicewifi;

import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Giacomo on 06/12/2016.
 */

public class ClusterMaker extends ActivityMaps implements ClusterManager.OnClusterClickListener<MyMarker>, ClusterManager.OnClusterInfoWindowClickListener<MyMarker>, ClusterManager.OnClusterItemClickListener<MyMarker>, ClusterManager.OnClusterItemInfoWindowClickListener<MyMarker> {
    private Random mRandom = new Random(1984);
    ArrayList<HashMap<String, String>> mHotSpot ;
    public ClusterMaker (ArrayList<HashMap<String, String>> myList){
        super();
        mHotSpot = myList;

    }
    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */

    @Override
    public boolean onClusterClick(Cluster<MyMarker> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyMarker> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(MyMarker item) {
        // Does nothing, but you could go into the user's profile page, for example.
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyMarker item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    public void create() {

  /*      mClusterManager = new ClusterManager<MyMarker>(this, getMap());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
       mClusterManager.setOnClusterClickListener(this);
*/
        addItems();
        mClusterManager.cluster();
    }


    public void addItems() {
        for (int i = 0; i < mHotSpot.size() - 1; i++){
                MyMarker item = new MyMarker(Double.parseDouble(mHotSpot.get(i).get("lat")),
                        Double.parseDouble(mHotSpot.get(i).get("lon")),
                        mHotSpot.get(i).get("descrizione"),
                        mHotSpot.get(i).get("stato"),
                        R.drawable.gran);

                mClusterManager.addItem(item);
            }
    }
}


