package com.example.marcin.MeteoRG;

import android.app.ActionBar;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
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

public class MainActivity extends ActionBarActivity {


    //layout types//
    ImageButton refreshButton;
    TextView addressField, tempField, descriptionField, timeField;
    ImageView imageFlickrPhoto;
    //*****************//


    //location types//
    gps LocationObject;

    //flickr types//
    flickr FlickrObject;
    String FlickrTags;

    //weather//
    weather WeatherObject;


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
        tempField = (TextView) findViewById(R.id.temp);
        descriptionField = (TextView) findViewById(R.id.description);
        timeField = (TextView) findViewById(R.id.time);

        refreshButton.setOnClickListener(refreshButtonOnClickListener);

        letTheShowBegin();
    }


    private Button.OnClickListener refreshButtonOnClickListener = new Button.OnClickListener() {
        public void onClick(View arg0) {
            vibra(50);
            letTheShowBegin();
        }
    };

    public void vibra(int time){
        vibra = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibra.vibrate(time);
    }

    public void letTheShowBegin(){

        time = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        timeString = sdf.format(time);
        timeField.setText(timeString);

        refreshLocation();

        WeatherObject = new weather(clearString(LocationObject.City));

        FlickrTags = createFlickrTags();
        FlickrObject = new flickr(FlickrTags);

        if (FlickrObject.bmFlickr != null){
            imageFlickrPhoto.setImageBitmap(FlickrObject.bmFlickr);


            /////////////////////////////DOROBIĆ DOMYŚLNE!!!!!!///////////////
        }

        tempField.setText((Integer.toString(WeatherObject.temp))+"\u00b0"+"C");
        descriptionField.setText(WeatherObject.conditions);
    }
    
    public String createFlickrTags(){
        String tagsString = null;
        if(WeatherObject.weatherUpdateTime > WeatherObject.sunsetTime && WeatherObject.weatherUpdateTime < WeatherObject.sunriseTime)
        {
            tagsString = clearString(WeatherObject.conditionsShort+" "+LocationObject.City);
        }
        else
        {
            tagsString = clearString(WeatherObject.conditionsShort+",night"+" "+LocationObject.City);
        }
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
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return newString;
    }







}