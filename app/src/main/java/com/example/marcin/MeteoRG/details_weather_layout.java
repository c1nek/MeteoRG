package com.example.marcin.MeteoRG;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Marcin on 2015-03-28.
 */
public class details_weather_layout extends Fragment{
    View mainView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.details_weather, container, false);

        return mainView;
    }
}