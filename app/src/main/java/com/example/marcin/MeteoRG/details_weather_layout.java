package com.example.marcin.MeteoRG;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by Marcin on 2015-03-28.
 */
public class details_weather_layout extends Fragment{
    View mainView;
    weather WeatherObjectFragment2;

    TextView maxtempField, mintempField;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.details_weather, container, false);


        maxtempField = (TextView) mainView.findViewById(R.id.maxTemp);
        mintempField = (TextView) mainView.findViewById(R.id.minTemp);

        WeatherObjectFragment2 = ((MainActivity)getActivity()).getwWather();
        fillWithData();

        return mainView;
    }

    public void fillWithData()
    {
        //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        //String sunriseTimeString = sdf.format(WeatherObjectFragment2.sunriseTime);

        mintempField.setText((Integer.toString(WeatherObjectFragment2.tempMin))+"\u00b0"+"C");
        maxtempField.setText((Integer.toString(WeatherObjectFragment2.tempMax))+"\u00b0"+"C");


    }
}