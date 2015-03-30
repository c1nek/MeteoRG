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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        LocationObject = new gps(this);

        refreshButton = (ImageButton)findViewById(R.id.Refresh);
        imageFlickrPhoto = (ImageView)findViewById(R.id.flickrPhoto);
        addressField = (TextView) findViewById(R.id.gpscity);
        timeField = (TextView) findViewById(R.id.time);
        refreshButton.setOnClickListener(refreshButtonOnClickListener);

        letTheShowBegin();
        this.initialisePaging();


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
            letTheShowBegin();

           // basicFragment = (basic_weather_layout) getSupportFragmentManager().findFragmentById(R.id.);

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

        WeatherObject = new weather(clearString(LocationObject.City));

        FlickrTags = createFlickrTags();
        FlickrObject = new flickr(FlickrTags);

        if (FlickrObject.bmFlickr != null){
            imageFlickrPhoto.setImageBitmap(FlickrObject.bmFlickr);
        }
        else{
            FlickrObject = new flickr(WeatherObject.conditionsShort);
        }
        clockUpdateThread.start();
    }
    
    public String createFlickrTags(){
        String tagsString = null;
        //////////CALY CZAS CIEMNO, BO UPDATETIME ZWRACA W UTC////////////////
        //if(WeatherObject.weatherUpdateTime.getTime() > WeatherObject.sunsetTime.getTime() && WeatherObject.weatherUpdateTime.getTime() < WeatherObject.sunriseTime.getTime())
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
        LocationObject.getLocation();
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







}