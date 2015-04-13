package com.example.marcin.MeteoRG;

/**
 * Created by Marcin on 2015-04-08.
 */
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class map_weather extends MapFragment {

    static final LatLng HAMBURG = new LatLng(53.558, 9.927);
    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
             //   .getMap();

        //Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
             //   .title("Hamburg"));



        // Move the camera instantly to hamburg with a zoom of 15.
       // map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

        // Zoom in, animating the camera.
      //  map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        //...


    }
}