package com.example.marcin.MeteoRG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Marcin on 2015-03-28.
 */
public class details_weather_layout extends Fragment{
    View mainView;
    weather WeatherObjectFragment2;

    TextView maxtempField, mintempField,humFiled,pressField,sunsetTimeField,sunriceTimeFiled, moonAgeField, moonPrecentField;

    ImageView moonImage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.details_weather, container, false);


        maxtempField = (TextView) mainView.findViewById(R.id.maxTemp);
        mintempField = (TextView) mainView.findViewById(R.id.minTemp);
        humFiled = (TextView) mainView.findViewById(R.id.hum);
        pressField = (TextView) mainView.findViewById(R.id.press);
        sunsetTimeField = (TextView) mainView.findViewById(R.id.sunset);
        sunriceTimeFiled = (TextView) mainView.findViewById(R.id.sunrise);
        moonAgeField = (TextView) mainView.findViewById(R.id.moonAgeA);
        moonPrecentField = (TextView) mainView.findViewById(R.id.moonPrecentageA);

        moonImage = (ImageView) mainView.findViewById(R.id.moonImage);

        WeatherObjectFragment2 = ((MainActivity)getActivity()).getwWather();
        fillWithData();

        return mainView;
    }

    public void fillWithData()
    {
        new Thread(LoadPhotoFromURLThread).start();
        try {
            WeatherObjectFragment2 = ((MainActivity)getActivity()).getwWather();
            mintempField.setText((Integer.toString(WeatherObjectFragment2.tempMin)) + "\u00b0" + "C");
            maxtempField.setText((Integer.toString(WeatherObjectFragment2.tempMax)) + "\u00b0" + "C");
            humFiled.setText(WeatherObjectFragment2.humidity);
            pressField.setText((Integer.toString(WeatherObjectFragment2.pressure)) + " hPa");

            String formattedSunsetDate = WeatherObjectFragment2.sunsetTimeHour + ":" + String.format("%02d", WeatherObjectFragment2.sunsetTimeMin);
            String formattedSunriceDate  = WeatherObjectFragment2.sunriseTimeHour + ":" + String.format("%02d",WeatherObjectFragment2.sunriseTimeMin);

            sunsetTimeField.setText(formattedSunriceDate);
            sunriceTimeFiled.setText(formattedSunsetDate);

            moonAgeField.setText(WeatherObjectFragment2.moonAge + " dni");
            moonPrecentField.setText(WeatherObjectFragment2.moonPercent + "%");
        }
        catch (Exception e){}

    }

    public Bitmap roundBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

       int radius = Math.min((h / 2)-2, (w / 2)-2);
        Bitmap output = Bitmap.createBitmap(w + 4, h + 4, Bitmap.Config.ARGB_8888);

        Paint p = new Paint();
        p.setAntiAlias(true);

        Canvas c = new Canvas(output);
        c.drawARGB(0, 0, 0, 0);
        p.setStyle(Paint.Style.FILL);

        c.drawCircle((w / 2) + 4, (h / 2) + 4, radius, p);

        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        c.drawBitmap(bitmap, 4, 4, p);
        p.setXfermode(null);



        return output;
    }

    private Runnable LoadPhotoFromURLThread = new Runnable() {

        public void run()
        {
            try {
                URL Url = new URL(WeatherObjectFragment2.moonImageURL);
                WeatherObjectFragment2.moonImage = BitmapFactory.decodeStream(Url.openConnection().getInputStream());
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        moonImage.setImageBitmap(roundBitmap(WeatherObjectFragment2.moonImage));
                    }});
                }catch (MalformedURLException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
}