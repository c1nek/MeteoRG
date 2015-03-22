package com.example.marcin.myfirstapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.StrictMode;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {


    //layout types//
    EditText searchText;
    Button searchButton;
    TextView latituteField,longitudeField,addressField;
    ImageView imageFlickrPhoto;
    //*****************//


    //location types//
    gps LocationObject;

    //flickr types//
    flickr FlickrObject;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        LocationObject = new gps(this);


        searchText = (EditText)findViewById(R.id.searchtext);
        searchButton = (Button)findViewById(R.id.searchbutton);
        imageFlickrPhoto = (ImageView)findViewById(R.id.flickrPhoto);
        latituteField = (TextView) findViewById(R.id.gpslong);
        longitudeField = (TextView) findViewById(R.id.gpslat);
        addressField = (TextView) findViewById(R.id.gpscity);


        searchButton.setOnClickListener(searchButtonOnClickListener);
    }



    private Button.OnClickListener searchButtonOnClickListener = new Button.OnClickListener(){

        public void onClick(View arg0) {

            refreshLocation();

            String searchQ = searchText.getText().toString();
            FlickrObject = new flickr(searchQ);

            if (FlickrObject.bmFlickr != null){
                imageFlickrPhoto.setImageBitmap(FlickrObject.bmFlickr);
            }
        }};

    public void refreshLocation(){
        LocationObject.getLocation();
        latituteField.setText(Double.toString(LocationObject.latitude));
        longitudeField.setText(Double.toString(LocationObject.longitude));
        addressField.setText(LocationObject.City);
    }







}