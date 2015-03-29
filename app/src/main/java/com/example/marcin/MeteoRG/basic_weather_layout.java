package com.example.marcin.MeteoRG;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;


/**
 * Created by Marcin on 2015-03-28.
 */
public class basic_weather_layout extends Fragment{

    View mainView;
    weather WeatherObjectFragment1;


   TextView tempField,descriptionField, sunriseTimeFiled;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.basic_weather, container, false);

        tempField = (TextView) mainView.findViewById(R.id.temp);
        descriptionField = (TextView) mainView.findViewById(R.id.description);
        sunriseTimeFiled = (TextView) mainView.findViewById(R.id.sunriseTime);

        WeatherObjectFragment1 = ((MainActivity)getActivity()).getwWather();
        fillWithData();


        return mainView;
    }


    public void fillWithData()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String sunriseTimeString = sdf.format(WeatherObjectFragment1.sunriseTime);

        tempField.setText((Integer.toString(WeatherObjectFragment1.temp))+"\u00b0"+"C");
        descriptionField.setText(WeatherObjectFragment1.conditions);
        sunriseTimeFiled.setText(sunriseTimeString);
    }
    }






