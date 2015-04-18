package com.example.marcin.MeteoRG;

/**
 * Created by Marcin on 2015-03-21.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by Marcin on 2015-03-21.
 */
public class gps extends Service implements LocationListener{

    private Context mContext = null;

    boolean canGetLocation = false;

    boolean GPS_WLACZONY = false;
    boolean SIEC_WLACZONA = false;

    Location location = null;

    double latitude;
    double longitude;

    String City = "";
    String Country = "";

    // odleglosc do update (metry)
    private static final long MIN_ODLEGLOSC = 1000;

    // minimalny czas poimedzy update (ms)
    private static final long MIN_TIME = 1000 * 60; // 1 minuta

    protected LocationManager locationManager = null;

    public gps (Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {

        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            GPS_WLACZONY = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            SIEC_WLACZONA  = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!GPS_WLACZONY && !SIEC_WLACZONA ) {
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (SIEC_WLACZONA ) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME ,MIN_ODLEGLOSC, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            getCity(latitude,longitude);
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (GPS_WLACZONY) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_ODLEGLOSC, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                getCity(latitude,longitude);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public String getCity(double lat, double lon){

        String city = null, country = null;
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            city = addresses.get(0).getLocality();
            country = addresses.get(0).getCountryCode();
        }
        Country = country;
        City = city;
        return city;
    }

    public void getCoord(String city) throws IOException {

        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());

        List<Address> addresses = null;

        try {
            addresses = gcd.getFromLocationName(city, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            latitude = addresses.get(0).getLatitude();
            longitude = addresses.get(0).getLongitude();
            City = addresses.get(0).getLocality();
            Country = addresses.get(0).getCountryCode();
        }

    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }



}
