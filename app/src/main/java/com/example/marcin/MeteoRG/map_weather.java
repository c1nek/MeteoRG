package com.example.marcin.MeteoRG;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.SupportMapFragment;

public class map_weather extends Fragment {

    View mainView;
    SupportMapFragment map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.maps_activity, container, false);

        //map = (SupportMapFragment) mainView.findViewById(R.id.map);



        return mainView;
    }



}