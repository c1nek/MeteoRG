package com.example.marcin.myfirstapplication;

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
        double temp = 0;
        double pressure = 0;
        String conditions = null;



    public weather(String tags){
        this.tags = tags;
        ParseJSON(QueryWeather(tags));
    }

    private String QueryWeather(String q){

        String qResult = null;

        String qString =
                URL + "Poznań";
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

            JSONObject JSONObject_main = JsonObject.getJSONObject("main");
            temp = JSONObject_main.getDouble("temp");
            pressure = JSONObject_main.getDouble("prssure");

            JSONObject JSONObject_weather = JsonObject.getJSONObject("weather");
            conditions = JSONObject_weather.getString("description");

          } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
