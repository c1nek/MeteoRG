package com.example.marcin.MeteoRG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Marcin on 2015-03-22.
 */
public class flickr {
    //flickr strings//
    String FlickrQuery_url = "https://api.flickr.com/services/rest/?method=flickr.photos.search";
    String FlickrQuery_per_page = "&per_page=1";
    String FlickrQuery_nojsoncallback = "&nojsoncallback=1";
    String FlickrQuery_format = "&format=json";
    String FlickrQuery_tag = "&tags=";
    String FlickrQuery_key = "&api_key=";
    String FlickrApiKey = "64c0f179f8aec0444033c8b2c57a7db0";
    String FlickrSort = "&sort=interestingness-desc";
    String FlickrContentType = "&content_type=1";
    String lat, lon;
    //*****************//
    String Tags;
    String searchResult;
    String jsonResult;


    Bitmap bmFlickr;

    public flickr(String tags){
        this.Tags = tags;
        QueryFlickr(tags);
        ParseJSON(searchResult);
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
        searchResult = qResult;
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

            LoadPhotoFromFlickr(flickrId, flickrOwner, flickrSecret,flickrServer, flickrFarm, flickrTitle);

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
        bmFlickr = bm;
        return bm;
    }



}
