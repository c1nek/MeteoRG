package com.example.marcin.MeteoRG;

import android.graphics.Bitmap;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin on 2015-03-23.
 */
class forecastDay {
    private String day;
    private String icoURL;
    private int tempMax;
    private int tempMin;

    public forecastDay(String day, String URL, int max, int min){
        this.day = day;
        this.icoURL = URL;
        this.tempMax = max;
        this.tempMin = min;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIcoURL() {
        return icoURL;
    }

    public void setIcoURL(String icoURL) {
        this.icoURL = icoURL;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }
}

public class weather {


    String format = ".json";
    String astronomyQuery = "http://api.wunderground.com/api/abd32b1ab11c6808/astronomy/q/";
    String conditionsQuery = "http://api.wunderground.com/api/abd32b1ab11c6808/conditions/lang:PL/q/";
    String forecastQuery = "http://api.wunderground.com/api/abd32b1ab11c6808/forecast10day/lang:PL/q/";
    String tags = null;

    public List<forecastDay> listDay = new ArrayList<forecastDay>();

    //weather deatils strings//
    int temp = 0;
    int feelsLikeTemp = 0;
    int pressure = 0;
    String windSpeed = null;
    String visibility = null;
    String humidity = null;
    String conditions = null;
    String conditionsShort = null;

    //sun details//
    int sunriseTimeMin = 0;
    int sunriseTimeHour = 0;
    int sunsetTimeMin = 0;
    int sunsetTimeHour = 0;

    //moon details//
    int moonAge = 0;
    int moonPercent = 0;
    Bitmap moonImage = null;
    String moonImageURL = null;

    //time ///
    long timeOffset = 0;

    public weather(double lat, double lng) {

        tags= String.valueOf(lat) + "," + String.valueOf(lng);
        android.util.Log.i("weather", "q1");
        ParseJSONAstronomy(QueryWeather(astronomyQuery + tags + format));
        android.util.Log.i("weather", "q2");
        ParseJSONCondistios(QueryWeather(conditionsQuery + tags + format));
        android.util.Log.i("weather", "q3");
        ParseJSONForecast(QueryWeather(forecastQuery + tags + format));
        android.util.Log.i("weather", "f");
    }

    private String QueryWeather(String q) {

        String qResult = null;

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(q);

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

    private void ParseJSONAstronomy(String json){
        try
        {
        JSONObject JsonObjectA = new JSONObject(json);
            JSONObject JSONObject_moon_phase = JsonObjectA.getJSONObject("moon_phase");
                    moonPercent = JSONObject_moon_phase.getInt("percentIlluminated");
                    moonAge = JSONObject_moon_phase.getInt("ageOfMoon");
                    moonImageURL = "http://www.imooncal.com/cs/i/1_m"+moonAge+".jpg";


            JSONObject JSONObject_sun_phase = JsonObjectA.getJSONObject("sun_phase");
                JSONObject JSONObject_sunrise = JSONObject_sun_phase.getJSONObject("sunrise");
                        sunriseTimeHour = JSONObject_sunrise.getInt("hour");
                        sunriseTimeMin = JSONObject_sunrise.getInt("minute");
            JSONObject JSONObject_sunset = JSONObject_sun_phase.getJSONObject("sunset");
                        sunsetTimeHour = JSONObject_sunset.getInt("hour");
                        sunsetTimeMin = JSONObject_sunset.getInt("minute");
        }
        catch (JSONException e)
        {
        e.printStackTrace();
        }
    }

    private void ParseJSONCondistios(String json){
        try
        {
            JSONObject JsonObjectW = new JSONObject(json);

            // JSONObject JSONObject_response = JsonObject.getJSONObject("response");

            JSONObject JSONObject_current_observation = JsonObjectW.getJSONObject("current_observation");
            String timeOffset_s = (JSONObject_current_observation.getString("local_tz_offset"));
            timeOffset = (Long.parseLong(timeOffset_s.substring(0,3))*3600000)-7200000;
            temp = (JSONObject_current_observation.getInt("temp_c"));
            pressure = JSONObject_current_observation.getInt("pressure_mb");
            humidity = JSONObject_current_observation.getString("relative_humidity");
            windSpeed = (JSONObject_current_observation.getString("wind_kph"));
            conditions = JSONObject_current_observation.getString("weather");
            feelsLikeTemp = JSONObject_current_observation.getInt("feelslike_c");
            visibility = JSONObject_current_observation.getString("visibility_km");
            conditionsShort = getShortCond(JSONObject_current_observation.getString("icon"));
            }
        catch (JSONException e)
        {
        e.printStackTrace();
        }
    }

    private void ParseJSONForecast(String json){
        try
        {
            JSONObject JsonObjectW = new JSONObject(json);
                JSONObject JSONObject_current_forecast = JsonObjectW.getJSONObject("forecast");
                    JSONObject JSONObject_simpleforecast = JSONObject_current_forecast.getJSONObject("simpleforecast");
                        JSONArray JSONObject_forecastday = JSONObject_simpleforecast.getJSONArray("forecastday");
                            for(int i = 0; i < JSONObject_forecastday.length(); i++){
                                JSONObject JSONObject_oneday = JSONObject_forecastday.getJSONObject(i);
                                    JSONObject JSONObject_date = JSONObject_oneday.getJSONObject("date");
                                        String tempDay = JSONObject_date.getString("weekday");
                                    JSONObject JSONObject_high = JSONObject_oneday.getJSONObject("high");
                                        int temMax = JSONObject_high.getInt("celsius");
                                    JSONObject JSONObject_low = JSONObject_oneday.getJSONObject("low");
                                        int tempMin = JSONObject_low.getInt("celsius");
                                String tempURL = changeIconString(JSONObject_oneday.getString("icon_url"));
                                forecastDay tempforecastday = new forecastDay(tempDay, tempURL, temMax, tempMin);
                                listDay.add(tempforecastday);
                            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    private String changeIconString(String url){

        StringBuilder SB = new StringBuilder(url);
        SB.setCharAt(26, 'i');
        String newUrl = new String(SB);

        return newUrl;
    }

    private String getShortCond(String in){
        String Out = null;
        if(in.equals("chanceflurries") || in.equals("chancerain") || in.equals("chancesleet") || in.equals("tstorms") || in.equals("chancetstorms") || in.equals("flurries") || in.equals("sleet") || in.equals("rain"))
        {
            return Out = "rain,";
        }
        else if(in.equals("chancesnow") || in.equals("snow"))
        {
            return Out = "snow,";
        }
        else if(in.equals("clear") || in.equals("sunny") || in.equals("partlysunny") || in.equals("mostlysunny"))
        {
            return Out = "sunny,";
        }
        else if(in.equals("cloudy") || in.equals("partlycloudy") || in.equals("mostlycloudy"))
        {
            return Out = "cloudy,";
        }
        else if(in.equals("fog") || in.equals("hazy"))
        {
            return Out = "fog,";
        }
        else
        {
            return Out = "";
        }

    }




}







