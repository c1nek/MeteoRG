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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import android.util.Log;

class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

    ArrayList<String> resultList;

    Context mContext;
    int mResource;

    PlaceAPI mPlaceAPI = new PlaceAPI();

    public PlacesAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);

        mContext = context;
        mResource = resource;
    }

    @Override
    public int getCount() {
        // Last item will be the footer
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = mPlaceAPI.autocomplete(constraint.toString());

                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;
    }
}


public class MainActivity extends FragmentActivity {


    //weather & location types//
    double globalLat;
    double globalLen;
    String globalCity;
    String globalCountry;
    String adress;

    //pager adapter//
    private PagerAdapter mPagerAdapter;
    //*****************//


    //layout types//
    ImageButton refreshButton;
    ImageButton locationButton;
    ImageButton backButton;

    AutoCompleteTextView searchText;
    public TextView addressField, timeField;
    ImageView imageFlickrPhoto;
    //*****************//

    //location types//
    gps LocationObject;


    //flickr types//
    flickr FlickrObject;
    String FlickrTags;

    //weather//
    weather WeatherObject;

    //fragment types//
    List<Fragment> fragments;
    int id1, id2;

    basic_weather_layout basicFragment;
    details_weather_layout detailsFragment;

    //system types//
    Vibrator vibra;

    //time type//
    Date timeBasedOnTimezone = null;
    String timeString = null;
    int actualHour = 0;
    int actualMin = 0;


    long stoperStrat;
    long stoperStop;

    String str;

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

        //TODO splasz skrin

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        stoperStrat = System.currentTimeMillis();

        imageFlickrPhoto = (ImageView) findViewById(R.id.flickrPhoto);
        addressField = (TextView) findViewById(R.id.gpscity);
        timeField = (TextView) findViewById(R.id.time);

        refreshButton = (ImageButton) findViewById(R.id.Refresh);
        refreshButton.setOnClickListener(refreshButtonOnClickListener);

        locationButton = (ImageButton) findViewById(R.id.locationButton);
        locationButton.setOnClickListener(locationButtonOnClickListener);
        locationButton.setOnLongClickListener(locationButtonOnLongClickListener);

        backButton = (ImageButton) findViewById(R.id.back);
        backButton.setOnClickListener(backButtonOnClickListener);

        searchText = (AutoCompleteTextView) findViewById(R.id.searchEditText);
        searchText.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_list_item));

        searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                str = (String) adapterView.getItemAtPosition(position);
                searchText.setText(str);
                vibra(50);
                hideKeybord(view);
                try
                {
                    LocationObject.getCoord(str);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                globalLat = LocationObject.latitude;
                globalLen = LocationObject.longitude;
                globalCity = LocationObject.City;
                globalCountry = LocationObject.Country;

                searchText.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                locationButton.setVisibility(View.VISIBLE);
                refreshButton.setVisibility(View.VISIBLE);
                addressField.setVisibility(View.VISIBLE);
                timeField.setVisibility(View.VISIBLE);

                getWeather();
                setActivityFields();
                fillDataFragments();
                new Thread(getFlickrThread).start();
            }
       });

        this.initialisePaging();
        getLocation();
        setActivityFields();
        getWeather();
        setDefaultImage();
        clockUpdateThread.start();
        new Thread(getFlickrThread).start();
        stoperStop = System.currentTimeMillis();
        Log.i("Czas uruchomienia", String.valueOf((stoperStop - stoperStrat) / 1000));
    }

    public weather getwWather() {
        return WeatherObject;
    }

    private void initialisePaging() {

        fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, basic_weather_layout.class.getName()));
        fragments.add(Fragment.instantiate(this, details_weather_layout.class.getName()));

        this.mPagerAdapter = new myPageAdapter(super.getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) super.findViewById(R.id.pager);
        pager.setAdapter(this.mPagerAdapter);

        Fragment fragment1 = fragments.get(0);
        id1 = fragment1.getId();

        Fragment fragment2 = fragments.get(1);
        id2 = fragment1.getId();
    }

    ////////BUTTONS///////////

    private Button.OnClickListener refreshButtonOnClickListener = new Button.OnClickListener() {
        public void onClick(View arg0) {
            vibra(50);
            getWeather();
            fillDataFragments();
            new Thread(getFlickrThread).start();
            //new Thread(refreshOnActualLocationThread).start();
        }
    };

    private Button.OnClickListener locationButtonOnClickListener = new Button.OnClickListener() {
        public void onClick(View arg0) {
            vibra(50);
            getLocation();
            setActivityFields();
            getWeather();
            fillDataFragments();
            new Thread(getFlickrThread).start();
            //new Thread(startOnMyLocationThread).start();
        }
    };

    private Button.OnLongClickListener locationButtonOnLongClickListener = new Button.OnLongClickListener() {
        public boolean onLongClick(View arg0) {
            vibra(50);
            searchText.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.VISIBLE);
            locationButton.setVisibility(View.INVISIBLE);
            refreshButton.setVisibility(View.INVISIBLE);
            addressField.setVisibility(View.INVISIBLE);
            timeField.setVisibility(View.INVISIBLE);
            searchText.setText("");
            //letTheShowBegin();
            return true;
        }
    };

    private Button.OnClickListener backButtonOnClickListener = new Button.OnClickListener() {
        public void onClick(View arg0) {
            vibra(50);
            searchText.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.INVISIBLE);
            locationButton.setVisibility(View.VISIBLE);
            refreshButton.setVisibility(View.VISIBLE);
            addressField.setVisibility(View.VISIBLE);
            timeField.setVisibility(View.VISIBLE);
            hideKeybord(arg0);
        }
    };


    public void vibra(int time) {
        vibra = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibra.vibrate(time);
    }

    public void hideKeybord(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
    }

    ///////////////END BUTTONS///////////////
    public void getLocation(){
        LocationObject = new gps(this);
        globalLat = LocationObject.latitude;
        globalLen = LocationObject.longitude;
        globalCity = LocationObject.City;
        globalCountry = LocationObject.Country;
    }

    public void setActivityFields(){
        addressField.setText(globalCity+ ", "+globalCountry);
    }

    public void getWeather(){
        WeatherObject = new weather(globalLat, globalLen);
    }


    public void fillDataFragments(){
        basic_weather_layout basicLayout = (basic_weather_layout) fragments.get(0);
        basicLayout.fillWithData();
        details_weather_layout detailLayout = (details_weather_layout) fragments.get(1);
        detailLayout.fillWithData();
    }

    public String createFlickrTags(){
        String tagsString = null;

        //TODO actual time = null

            if ((actualHour < WeatherObject.sunsetTimeHour) && (actualHour > WeatherObject.sunriseTimeHour) && (actualMin < WeatherObject.sunsetTimeMin) && (actualMin > WeatherObject.sunriseTimeMin)) {
                tagsString = clearString(WeatherObject.conditionsShort + globalCity);
            } else {
                tagsString = clearString("night," + globalCity);
            }
        Log.i("weather time:", "SUNSET " + WeatherObject.sunsetTimeHour + ":" + WeatherObject.sunsetTimeMin + " SUNRISR " + WeatherObject.sunriseTimeHour + ":" + WeatherObject.sunriseTimeMin);
        Log.i("flickr tagz time:", actualHour + ":" + actualMin);
        Log.i("flickr tagz:", tagsString);


        //TODO night mode flickr

        return tagsString;
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

    public void setDefaultImage(){
        if(WeatherObject.conditionsShort.equals("rain,"))
        {
            imageFlickrPhoto.setImageResource(R.drawable.rain);
        }
        else if(WeatherObject.conditionsShort.equals("snow,"))
        {
            imageFlickrPhoto.setImageResource(R.drawable.snow);
        }
        else if(WeatherObject.conditionsShort.equals("sunny,"))
        {
            imageFlickrPhoto.setImageResource(R.drawable.sunny);
        }
        else if(WeatherObject.conditionsShort.equals("cloudy,"))
        {
            imageFlickrPhoto.setImageResource(R.drawable.cloudy);
        }
        else if(WeatherObject.conditionsShort.equals("fog,"))
        {
            imageFlickrPhoto.setImageResource(R.drawable.fog);
        }
        else
        {
            imageFlickrPhoto.setImageResource(R.drawable.cloudy);
        }


    }

    public void clockUpdate(){
        Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        long utcTime = cal1.getTimeInMillis();
        timeBasedOnTimezone = new Date(utcTime + WeatherObject.timeOffset);

        actualHour = timeBasedOnTimezone.getHours();
        actualMin = timeBasedOnTimezone.getMinutes();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        timeString = sdf.format(timeBasedOnTimezone);
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

    private Runnable getFlickrThread = new Runnable() {

        public void run()
        {
            Log.i("Flickr", "Flickr thread start");
            FlickrTags = createFlickrTags();
            FlickrObject = new flickr(FlickrTags, LocationObject.latitude, LocationObject.longitude);

            if (FlickrObject.bmFlickr != null) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        imageFlickrPhoto.setImageBitmap(FlickrObject.bmFlickr);
                    }
                });
            }
             else{
                FlickrTags = WeatherObject.conditionsShort;
                new Thread(getFlickrThread).start();
            }
            Log.i("Flickr", "Flickr thread stop");
        }
    };
}