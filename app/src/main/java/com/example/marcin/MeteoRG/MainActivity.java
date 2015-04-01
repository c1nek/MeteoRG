package com.example.marcin.MeteoRG;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;

import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.util.Log;
import android.widget.Toast;


import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
    public static weather WeatherObject;

    //fragment types//
    List<Fragment> fragments;
    int id1, id2;

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

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        stoperStrat = System.currentTimeMillis();

        Log.i("info", "Utworzono location object");

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
                String str = (String) adapterView.getItemAtPosition(position);
                vibra(50);
                Log.i("!!!!!!!", str);
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

                LocationObject.getCity(globalLat, globalLen);

                globalCity = LocationObject.City;
                globalCountry = LocationObject.Country;
                Log.i("!!!!!!!", globalCity + " " +globalCountry );

                searchText.setVisibility(View.INVISIBLE);
                backButton.setVisibility(View.INVISIBLE);
                locationButton.setVisibility(View.VISIBLE);
                refreshButton.setVisibility(View.VISIBLE);
                addressField.setVisibility(View.VISIBLE);
                timeField.setVisibility(View.VISIBLE);

                getWeather();
                setAdressField(globalCity + ", " + globalCountry);
                getFlickr();
                hideKeybord(view);
                fillDataFragments();




            }
       });


        imageFlickrPhoto = (ImageView) findViewById(R.id.flickrPhoto);
        addressField = (TextView) findViewById(R.id.gpscity);
        timeField = (TextView) findViewById(R.id.time);

        clockUpdateThread.start();

        letTheShowBegin();
        this.initialisePaging();
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
        }
    };

    private Button.OnClickListener locationButtonOnClickListener = new Button.OnClickListener() {
        public void onClick(View arg0) {
            vibra(50);
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
        }
    };


    public void vibra(int time) {
        vibra = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibra.vibrate(time);
    }
    public void hideKeybord(View view) {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    public void startOnMyLocation() {
    }

    public void refreshOnActualLocation() {
    }

    public void searchLocation() {
    }



    ///////////////END BUTTONS///////////////

    public void letTheShowBegin(){
        Log.i("info", "zdefinowano location");
        getLocation();
        Log.i("info", "zainicjalizowano location");

        setAdressField(globalCity + ", " + globalCountry);

        Log.i("info", "zdefinowano weather");
        getWeather();
        Log.i("info", "zainicjalizowano weather");

        Log.i("info", "zdefinowano flickr");
        getFlickr();
        Log.i("info", "zainicjalizowano flickr");
    }

    public void fillDataFragments(){
        basic_weather_layout basicLayout = (basic_weather_layout) fragments.get(0);
        basicLayout.fillWithData();
        details_weather_layout detailLayout = (details_weather_layout) fragments.get(1);
        detailLayout.fillWithData();



    }

    public void getLocation(){
        LocationObject = new gps(this);
        globalLat = LocationObject.latitude;
        globalLen = LocationObject.longitude;
        globalCity = LocationObject.City;
        globalCountry = LocationObject.Country;
    }

    public void setLocation(String locationName){
            }

    public void getWeather(){
        WeatherObject = new weather(globalLat, globalLen);
    }
    public void setDefaultBackgroung(){

    }

    public void getFlickr(){
        FlickrTags = createFlickrTags();
        FlickrObject = new flickr(FlickrTags);

        if (FlickrObject.bmFlickr != null){
            imageFlickrPhoto.setImageBitmap(FlickrObject.bmFlickr);
        }
        else{
            FlickrObject = new flickr(WeatherObject.conditionsShort);
            imageFlickrPhoto.setImageBitmap(FlickrObject.bmFlickr);
        }

    }




    public String createFlickrTags(){
        String tagsString = null;
        //////////CALY CZAS CIEMNO, BO UPDATETIME ZWRACA W UTC////////////////
        //if(WeatherObject.weatherUpdateTime > WeatherObject.sunsetTime && WeatherObject.weatherUpdateTime < WeatherObject.sunriseTime)
        //{
           tagsString = clearString(WeatherObject.conditionsShort+" "+globalCity);
        //}
        //else
        //{
            //otagsString = clearString(WeatherObject.conditionsShort+",night"+" "+LocationObject.City);
        //}
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

    public void setAdressField(String adress){
        addressField.setText(adress);
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