package com.example.marcin.MeteoRG;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Marcin on 2015-03-28.
 */
public class details_weather_layout extends Fragment{
    View mainView;
    weather WeatherObjectFragment2;

    TextView maxtempField, mintempField,humFiled,pressField,sunsetTimeField,sunriceTimeFiled;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.details_weather, container, false);

        maxtempField = (TextView) mainView.findViewById(R.id.maxTemp);
        mintempField = (TextView) mainView.findViewById(R.id.minTemp);
        humFiled = (TextView) mainView.findViewById(R.id.hum);
        pressField = (TextView) mainView.findViewById(R.id.press);
        sunsetTimeField = (TextView) mainView.findViewById(R.id.sunset);
        sunriceTimeFiled = (TextView) mainView.findViewById(R.id.sunrise);

        fillWithData();

        return mainView;
    }

    public void fillWithData()
    {
        WeatherObjectFragment2 = ((MainActivity)getActivity()).getwWather();
        try {
            mintempField.setText((Integer.toString(WeatherObjectFragment2.tempMin)) + "\u00b0" + "C");
            maxtempField.setText((Integer.toString(WeatherObjectFragment2.tempMax)) + "\u00b0" + "C");
            humFiled.setText(WeatherObjectFragment2.humidity);
            pressField.setText((Integer.toString(WeatherObjectFragment2.pressure)) + " hPa");

            String formattedSunsetDate = WeatherObjectFragment2.sunsetTimeHour + ":" + WeatherObjectFragment2.sunsetTimeMin;
            String formattedSunriceDate  = WeatherObjectFragment2.sunriseTimeHour + ":" + WeatherObjectFragment2.sunriseTimeMin;

            sunsetTimeField.setText(formattedSunriceDate);
            sunriceTimeFiled.setText(formattedSunsetDate);
        }
        catch (Exception e){}

    }
}