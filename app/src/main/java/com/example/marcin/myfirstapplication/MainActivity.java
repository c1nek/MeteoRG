package com.example.marcin.myfirstapplication;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Typeface;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {


    //flickr strings//
    String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    String FlickrQuery_per_page = "&per_page=1";
    String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
    String FlickrQuery_format = "&format=json";
    String FlickrQuery_tag = "&tags=";
    String FlickrQuery_key = "&api_key=";
    String FlickrApiKey = "64c0f179f8aec0444033c8b2c57a7db0";
    String FlickrSort = "&sort=relevance";
    String FlickrContentType = "&content_type=1";
    //*****************//


    //layout types//
    EditText searchText;
    Button searchButton;
    TextView latituteField,longitudeField,addressField;
    ImageView imageFlickrPhoto;
    Bitmap bmFlickr;
    //*****************//


    //location types//

    gps LocationObject;


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
            String searchResult = QueryFlickr(searchQ);
            String jsonResult = ParseJSON(searchResult);

            if (bmFlickr != null){
                imageFlickrPhoto.setImageBitmap(bmFlickr);

            }
        }};

    public void refreshLocation(){
        LocationObject.getLocation();

        latituteField.setText(Double.toString(LocationObject.latitude));
        longitudeField.setText(Double.toString(LocationObject.longitude));
        addressField.setText(LocationObject.City);

    }

    private String QueryFlickr(String q){

        String qResult = null;

        String qString =
                FlickrQuery_url
                        + FlickrQuery_per_page
                        + FlickrQuery_nojsoncallback
                        + FlickrQuery_format
                        + FlickrContentType
                        + FlickrSort
                        + FlickrQuery_tag + q
                        + FlickrQuery_key + FlickrApiKey;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(qString);

        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

            if (httpEntity != null){
                InputStream inputStream = httpEntity.getContent();
                Reader in = new InputStreamReader(inputStream);
                BufferedReader bufferedreader = new BufferedReader(in);
                StringBuilder stringBuilder = new StringBuilder();

                String stringReadLine = null;

                while ((stringReadLine = bufferedreader.readLine()) != null) {
                    stringBuilder.append(stringReadLine + "\n");
                }

                qResult = stringBuilder.toString();

            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return qResult;}


    private String ParseJSON(String json) {

        String jResult = null;
        bmFlickr = null;

        String flickrId;
        String flickrOwner;
        String flickrSecret;
        String flickrServer;
        String flickrFarm;
        String flickrTitle;

        try {
            JSONObject JsonObject = new JSONObject(json);
            JSONObject Json_photos = JsonObject.getJSONObject("photos");
            JSONArray JsonArray_photo = Json_photos.getJSONArray("photo");
            JSONObject FlickrPhoto = JsonArray_photo.getJSONObject(0);

            flickrId = FlickrPhoto.getString("id");
            flickrOwner = FlickrPhoto.getString("owner");
            flickrSecret = FlickrPhoto.getString("secret");
            flickrServer = FlickrPhoto.getString("server");
            flickrFarm = FlickrPhoto.getString("farm");
            flickrTitle = FlickrPhoto.getString("title");

            jResult = "\nid: " + FlickrPhoto.getString("id") + "\n"
                    + "owner: " + FlickrPhoto.getString("owner") + "\n"
                    + "secret: " + FlickrPhoto.getString("secret") + "\n"
                    + "server: " + FlickrPhoto.getString("server") + "\n"
                    + "farm: " + FlickrPhoto.getString("farm") + "\n"
                    + "title: " + FlickrPhoto.getString("title") + "\n";

            bmFlickr = LoadPhotoFromFlickr(flickrId, flickrOwner, flickrSecret,
                    flickrServer, flickrFarm, flickrTitle);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jResult;
    }

    private Bitmap LoadPhotoFromFlickr(
            String id, String owner, String secret,
            String server, String farm, String title){
        Bitmap bm= null;

        String FlickrPhotoPath =
                "http://farm" + farm + ".static.flickr.com/"
                        + server + "/" + id + "_" + secret + "_b.jpg";

        URL FlickrPhotoUrl = null;

        try {
            FlickrPhotoUrl = new URL(FlickrPhotoPath);

            HttpURLConnection httpConnection
                    = (HttpURLConnection) FlickrPhotoUrl.openConnection();
            httpConnection.setDoInput(true);
            httpConnection.connect();
            InputStream inputStream = httpConnection.getInputStream();
            bm = BitmapFactory.decodeStream(inputStream);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bm;
    }





}