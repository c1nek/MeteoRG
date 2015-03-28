package com.example.marcin.MeteoRG;

import android.os.Bundle;
import android.support.v4.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Marcin on 2015-03-28.
 */
public class basic_weather_layout extends Fragment{
    View mainView;

    public static TextView tempField;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.basic_weather, container, false);





        return mainView;
    }

    public void changeText(String text)
    {
        tempField = (TextView) mainView.findViewById(R.id.temp);
        tempField.setText(text);
    }


}
