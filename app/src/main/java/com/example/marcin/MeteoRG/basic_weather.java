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
public class basic_weather extends Fragment{

    View mainView;
    weather WeatherObjectFragment1;
    TextView tempField,descriptionField,feelsLikeField;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.basic_weather, container, false);

        tempField = (TextView) mainView.findViewById(R.id.temp);
        descriptionField = (TextView) mainView.findViewById(R.id.description);
        feelsLikeField = (TextView) mainView.findViewById(R.id.feelsLike);

        WeatherObjectFragment1 = ((MainActivity)getActivity()).getwWather();
        fillWithData();

        return mainView;
    }


    public void fillWithData()
    {
        WeatherObjectFragment1 = ((MainActivity)getActivity()).getwWather();
        try {
            tempField.setText((Integer.toString(WeatherObjectFragment1.temp)) + "\u00b0" + "C");
            feelsLikeField.setText("odczuwalna: " + (Integer.toString(WeatherObjectFragment1.feelsLikeTemp)) + "\u00b0" + "C");
            descriptionField.setText(WeatherObjectFragment1.conditions);
        }
        catch (Exception ex){

        }
    }
    }






