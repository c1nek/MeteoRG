package com.example.marcin.MeteoRG;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.util.Log;

public class MainActivity extends FragmentActivity {

    //pager adapter//
    private PagerAdapter mPagerAdapter;
    //*****************//

    //layout types//
    ImageButton refreshButton;
    public TextView addressField, timeField;
    ImageView imageFlickrPhoto;
    //*****************//

    //location types//
    gps LocationObject;

    //flickr types//
    flickr FlickrObject;
    String FlickrTags;

    //weather//
    public static weather WeatherObject;

    //fragment types//
    basic_weather_layout basicFragment;
    details_weather_layout detailsFragment;

    //system types//
    Vibrator vibra;

    //time type//
    Date time = null;
    String timeString = null;


    long stoperStrat;
    long stoperStop;

    //threads//
   // Thread clockUpdateThread;
   // Thread getGpsObject;
   // Thread getTimeZoneThread;
   // Thread getWeatherObject;
   // Thread getFlickrObject;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        stoperStrat = System.currentTimeMillis();

        LocationObject = new gps(this);

        Log.i("info", "Utworzono location object");

        refreshButton = (ImageButton)findViewById(R.id.Refresh);
        imageFlickrPhoto = (ImageView)findViewById(R.id.flickrPhoto);
        addressField = (TextView) findViewById(R.id.gpscity);
        timeField = (TextView) findViewById(R.id.time);
        refreshButton.setOnClickListener(refreshButtonOnClickListener);

        clockUpdateThread.start();

        letTheShowBegin();
        this.initialisePaging();
        stoperStop= System.currentTimeMillis();
        Log.i("Czas uruchomienia", String.valueOf((stoperStop - stoperStrat)/1000));
    }

    public weather getwWather(){
        return WeatherObject;
    }

    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, basic_weather_layout.class.getName()));
        fragments.add(Fragment.instantiate(this, details_weather_layout.class.getName()));

        this.mPagerAdapter = new myPageAdapter(super.getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) super.findViewById(R.id.pager);
        pager.setAdapter(this.mPagerAdapter);
    }

    private Button.OnClickListener refreshButtonOnClickListener = new Button.OnClickListener() {
        public void onClick(View arg0) {
            vibra(50);
            //clockUpdateThread.stop();
            letTheShowBegin();

           // basicFragment = (basic_weather_layout) getSupportFragmentManager().findFragmentById(R.id.);
//TODO odwolanie do metod na fragmencie
            //basicFragment.fillWithData();

           // detailsFragment = (details_weather_layout) getSupportFragmentManager().findFragmentByTag("frag2");
           // detailsFragment.fillWithData();


        }
    };

    public void vibra(int time){
        vibra = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibra.vibrate(time);
    }

    public void letTheShowBegin(){



        refreshLocation();

        Log.i("info", "zdefinowano weather object");

        createWeatherObject();

        Log.i("info", "zainicjalizowano weather object");

        FlickrTags = createFlickrTags();

        Log.i("info", "zdefinowano flickr object");

        FlickrObject = new flickr(FlickrTags);

        Log.i("info", "zainicjalizowano flickr object");

        if (FlickrObject.bmFlickr != null){
            imageFlickrPhoto.setImageBitmap(FlickrObject.bmFlickr);
        }
        else{
            FlickrObject = new flickr(WeatherObject.conditionsShort);
        }


    }

    private void createWeatherObject(){
        WeatherObject = new weather(LocationObject.latitude, LocationObject.longitude);
    }

    private void createFlickrObject(){



    }

    public String createFlickrTags(){
        String tagsString = null;
        //////////CALY CZAS CIEMNO, BO UPDATETIME ZWRACA W UTC////////////////
        //if(WeatherObject.weatherUpdateTime > WeatherObject.sunsetTime && WeatherObject.weatherUpdateTime < WeatherObject.sunriseTime)
        //{
           tagsString = clearString(WeatherObject.conditionsShort+" "+LocationObject.City);
        //}
        //else
        //{
            //otagsString = clearString(WeatherObject.conditionsShort+",night"+" "+LocationObject.City);
        //}
        return tagsString;
    }

    public void refreshLocation(){
        Log.i("info", "pobieranie lokalizacji");
        LocationObject.getLocation();
        Log.i("info", "pobranie lokalizacji");
        addressField.setText(LocationObject.City + ", " + LocationObject.Country);
    }

    public String clearString(String oldString){
        String newString = null;
        newString = oldString;
        try {
            newString = newString.replaceAll(" ", ",");
            newString = newString.replaceAll("ą", "a");
            newString = newString.replaceAll("ć", "c");
            newString = newString.replaceAll("ę", "e");
            newString = newString.replaceAll("ó", "o");
            newString = newString.replaceAll("ż", "z");
            newString = newString.replaceAll("ł", "l");
            newString = newString.replaceAll("ź", "z");
            newString = newString.replaceAll("ń", "n");
            newString = newString.replaceAll("ś", "s");
            newString = newString.replaceAll("null", "");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return newString;
    }

    public void clockUpdate(){
        time = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        timeString = sdf.format(time);
        timeField.setText(timeString);
    }

    Thread clockUpdateThread = new Thread() {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           clockUpdate();
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };

    Thread FlickrObjectThread = new Thread() {

        @Override
        public void run() {
            Log.i("info", "start getWeatherObject");
            createFlickrObject();
            Log.i("info", "end getWeatherObject");
        }

    };







}