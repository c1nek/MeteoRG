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
import java.util.Date;

/**
 * Created by Marcin on 2015-03-23.
 */
public class weather {


    String format = ".json";

    //weather deatils strings//
    int temp = 0;
    int tempMax = 0;
    int tempMin = 0;
    int pressure = 0;
    String humidity = null;
    String conditions = null;
    String conditionsShort = "rain";

    //weather times UTC//
    int sunriseTimeMin = 0;
    int sunriseTimeHour = 0;
    int sunsetTimeMin = 0;
    int sunsetTimeHour = 0;

    public weather(double lat, double lng) {
        String tags= String.valueOf(lat) + "," + String.valueOf(lng);
        ParseJSON(QueryWeather(tags), QueryAstronomy(tags));
    }

    private String QueryAstronomy(String q){
        String qResult = null;
        String URL = "http://api.wunderground.com/api/abd32b1ab11c6808/astronomy/q/";

        String qString = URL + q + format;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(qString);

        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

            if (httpEntity != null) {
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
        return qResult;
    }

    private String QueryWeather(String q) {

        String URL = "http://api.wunderground.com/api/abd32b1ab11c6808/conditions/lang:PL/q/";
        String tags = q;

        String qResult = null;

        String qString = URL + tags + format;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(qString);

        try {
            HttpEntity httpEntity = httpClient.execute(httpGet).getEntity();

            if (httpEntity != null) {
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
        return qResult;
    }


    // TODO jebnac wontki

    private void ParseJSON(String jsonW, String jsonA) {

        try {
            JSONObject JsonObjectW = new JSONObject(jsonW);

           // JSONObject JSONObject_response = JsonObject.getJSONObject("response");

            JSONObject JSONObject_current_observation = JsonObjectW.getJSONObject("current_observation");
                temp = (JSONObject_current_observation.getInt("temp_c"));
                pressure = JSONObject_current_observation.getInt("pressure_mb");
                humidity = JSONObject_current_observation.getString("relative_humidity");
                conditions = JSONObject_current_observation.getString("weather");

            JSONObject JsonObjectA = new JSONObject(jsonA);
                JSONObject JSONObject_sun_phase = JsonObjectA.getJSONObject("sun_phase");
                    JSONObject JSONObject_sunrise = JSONObject_sun_phase.getJSONObject("sunrise");
                        sunriseTimeHour = JSONObject_sunrise.getInt("hour");
                        sunriseTimeMin = JSONObject_sunrise.getInt("minute");
                    JSONObject JSONObject_sunset = JSONObject_sun_phase.getJSONObject("sunset");
                        sunsetTimeHour = JSONObject_sunset.getInt("hour");
                        sunsetTimeMin = JSONObject_sunset.getInt("minute");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}





