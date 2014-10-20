package com.agcy.eatwithme.Core.Helpers;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import com.agcy.eatwithme.Core.MyLocationListener;

public class FindMe {
    public static interface FindMeListener {
        public void foundLocation(String provider, Location location);

        public void couldntFindLocation();
    }

    public static FindMe please(Context context, FindMeListener listener) {
        FindMe findMe = new FindMe(context, listener);
        findMe.please(context);
        return findMe;
    }

    private LocationManager locationManager;
    private FindMeListener listener;
    Location location;

    public FindMe(Context context, FindMeListener listener) {
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.listener = listener;
    }

    public void please(Context mContext) {
        final MyLocationListener myLocationService = new MyLocationListener(mContext) {
        };
        if (locationManager == null) {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);
        }
        Boolean networkNotEmpty = false, gpsNotEmpty = false, passiveNotEmpty = false;
        Boolean isNetworkEnabled = false, isGPSEnabled = false;
        if (locationManager.getAllProviders().contains(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, 0, 100,
                    myLocationService);
            passiveNotEmpty = locationManager
                    .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null;
        }
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 100,
                    myLocationService);
            networkNotEmpty = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null;
        }
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 100, myLocationService);
            gpsNotEmpty = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER) != null;
        }

        if ((!isGPSEnabled) && ((isNetworkEnabled) || (passiveNotEmpty) || (networkNotEmpty))) {
            if (networkNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                listener.foundLocation(LocationManager.NETWORK_PROVIDER, location);
            }
            if (passiveNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                listener.foundLocation(LocationManager.PASSIVE_PROVIDER, location);
            }
            if (gpsNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                myLocationService.stopUsingGPS();
                listener.foundLocation(LocationManager.GPS_PROVIDER, location);
            }
            listener.couldntFindLocation();
        } else {
            if (gpsNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                myLocationService.stopUsingGPS();
                listener.foundLocation(LocationManager.GPS_PROVIDER, location);
            }
            if (passiveNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                listener.foundLocation(LocationManager.PASSIVE_PROVIDER, location);
            }
            if (networkNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                listener.foundLocation(LocationManager.NETWORK_PROVIDER, location);
            }
        }
        listener.couldntFindLocation();
    }

}