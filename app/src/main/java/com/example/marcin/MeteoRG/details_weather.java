package com.example.marcin.MeteoRG;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Marcin on 2015-03-28.
 */
public class details_weather extends Fragment
        //implements OnMapReadyCallback
{
    View mainView;
    weather WeatherObjectFragment2;
    gps LocationObjectFragment2;

    TextView visField, windField,humFiled,pressField,sunsetTimeField,sunriceTimeFiled, moonAgeField, moonPrecentField;

    ImageView moonImage;

    private FragmentActivity myContext;

    private GoogleMap map;
    Marker cityMarker;

    LatLng cityLatLng;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.details_weather, container, false);


        visField = (TextView) mainView.findViewById(R.id.visibility);
        windField = (TextView) mainView.findViewById(R.id.windSpeed);
        humFiled = (TextView) mainView.findViewById(R.id.hum);
        pressField = (TextView) mainView.findViewById(R.id.press);
        sunsetTimeField = (TextView) mainView.findViewById(R.id.sunset);
        sunriceTimeFiled = (TextView) mainView.findViewById(R.id.sunrise);
        moonAgeField = (TextView) mainView.findViewById(R.id.moonAgeA);
        moonPrecentField = (TextView) mainView.findViewById(R.id.moonPrecentageA);

        moonImage = (ImageView) mainView.findViewById(R.id.moonImage);

        FragmentManager fragManager = this.getChildFragmentManager();
        SupportMapFragment mapFrag = (SupportMapFragment) fragManager.findFragmentById(R.id.map);
        map = mapFrag.getMap();
       // map.getUiSettings().setAllGesturesEnabled(false);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        fillWithData();

        return mainView;
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    public void fillWithData()
    {
        new Thread(LoadPhotoFromURLThread).start();
        try {
            WeatherObjectFragment2 = ((MainActivity)getActivity()).getwWather();
            LocationObjectFragment2 = ((MainActivity)getActivity()).getGpsObject();
            if(WeatherObjectFragment2.visibility.equals("N/A")){
                visField.setText("-- km");
            }
            else {
                visField.setText(WeatherObjectFragment2.visibility + " km");
            }
            windField.setText(WeatherObjectFragment2.windSpeed + " km/h");
            humFiled.setText(WeatherObjectFragment2.humidity);
            pressField.setText((Integer.toString(WeatherObjectFragment2.pressure)) + " hPa");

            String formattedSunsetDate = WeatherObjectFragment2.sunsetTimeHour + ":" + String.format("%02d", WeatherObjectFragment2.sunsetTimeMin);
            String formattedSunriceDate  = WeatherObjectFragment2.sunriseTimeHour + ":" + String.format("%02d",WeatherObjectFragment2.sunriseTimeMin);

            sunsetTimeField.setText(formattedSunriceDate);
            sunriceTimeFiled.setText(formattedSunsetDate);

            moonAgeField.setText(WeatherObjectFragment2.moonAge + " dni");
            moonPrecentField.setText(WeatherObjectFragment2.moonPercent + "%");

            if (cityMarker != null) {
                cityMarker.remove();
            }

            cityLatLng = new LatLng(LocationObjectFragment2.latitude, LocationObjectFragment2.longitude);
            cityMarker = map.addMarker(new MarkerOptions().position(cityLatLng).title(LocationObjectFragment2.City));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityLatLng, 9));
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