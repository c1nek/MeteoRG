package com.example.marcin.MeteoRG;

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

/**
 * Created by Marcin on 2015-03-23.
 */
public class weather {

        String URL = "http://api.openweathermap.org/data/2.5/weather?q=";
        String tags = null;
        String searchResult = null;
        String JSONResult = null;

    //weather deatils strings//
        int temp= 0;
        int tempMax = 0;
        int tempMin = 0;
        int pressure = 0;
        int humidity = 0;
        String city = null;
        String conditions = null;
        String conditionsShort = null;

    //weather times UTC//
        int weatherUpdateTime = 0;
        int sunriseTime = 0;
        int sunsetTime = 0;

    public weather(String tags){
        this.tags = tags;
        ParseJSON(QueryWeather(tags));
    }

    private String QueryWeather(String q){

        String qResult = null;

        String lang = "&lang=pl";
        String units = "&units=metric";

        String qString =
                URL + q + units + lang;

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
        JSONResult = qResult;
        return qResult;}

    private void ParseJSON(String json) {

        try {
            JSONObject JsonObject = new JSONObject(json);

            JSONObject JSONObject_sys = JsonObject.getJSONObject("sys");
                sunriseTime = JSONObject_sys.getInt("sunrise");
                sunsetTime = JSONObject_sys.getInt("sunset");

            JSONArray JSONArray_weather = JsonObject.getJSONArray("weather");
            JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
                conditionsShort = JSONObject_weather.getString("main");
                conditions = JSONObject_weather.getString("description");

            JSONObject JSONObject_main = JsonObject.getJSONObject("main");
                temp = (JSONObject_main.getInt("temp"));
                pressure = JSONObject_main.getInt("pressure");
                tempMin= JSONObject_main.getInt("temp_min");
                tempMax= JSONObject_main.getInt("temp_max");
                humidity = JSONObject_main.getInt("humidity");



               weatherUpdateTime = JsonObject.getInt("dt");


          } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
