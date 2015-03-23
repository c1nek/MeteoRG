package com.example.marcin.myfirstapplication;

import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {


    //layout types//
    Button searchButton;
    TextView addressField,jsonWeather,tempField,descriptionField;
    ImageView imageFlickrPhoto;
    //*****************//


    //location types//
    gps LocationObject;

    //flickr types//
    flickr FlickrObject;
    String FlickrTags;

    //weather//
    weather WeatherObject;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        LocationObject = new gps(this);

        searchButton = (Button)findViewById(R.id.searchbutton);
        imageFlickrPhoto = (ImageView)findViewById(R.id.flickrPhoto);
        addressField = (TextView) findViewById(R.id.gpscity);
        tempField = (TextView) findViewById(R.id.temp);
        descriptionField = (TextView) findViewById(R.id.description);

        searchButton.setOnClickListener(searchButtonOnClickListener);
    }


    private Button.OnClickListener searchButtonOnClickListener = new Button.OnClickListener(){

        public void onClick(View arg0) {

            refreshLocation();

            WeatherObject = new weather(clearString(LocationObject.City));

            FlickrTags = clearString(WeatherObject.conditions+" "+LocationObject.City);
            FlickrObject = new flickr(FlickrTags);

            if (FlickrObject.bmFlickr != null){
                imageFlickrPhoto.setImageBitmap(FlickrObject.bmFlickr);


                /////////////////////////////DOROBIĆ DOMYŚLNE!!!!!!///////////////
            }

            tempField.setText((Integer.toString((int)WeatherObject.temp))+"\u00b0"+"C");
            descriptionField.setText(WeatherObject.conditions);
        }};

    public void refreshLocation(){
        LocationObject.getLocation();
        addressField.setText(LocationObject.City);
    }

    public String clearString(String oldString){
        String newString = oldString;
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

        return newString;
    }







}