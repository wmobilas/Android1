package build.agcy.test1.Core.Helpers;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class FindMe {
    public static interface FindMeListener {
        public void foundLocation(String provider, Location location);

        public void couldntFindLocation();
    }

    public static FindMe please(Context context, int timeout, boolean getLastKnownLocation, FindMeListener listener) {
        FindMe findMe = new FindMe(context, timeout, getLastKnownLocation, listener);
        findMe.please();
        return findMe;
    }

    private LocationManager locationManager;
    private FindMeListener listener;
    private int timeoutMS;
    private boolean getLastKnownLocation;

    public FindMe(Context context, int timeoutMS, boolean getLastKnownLocation, FindMeListener listener) {
        this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.listener = listener;
        this.timeoutMS = timeoutMS;
        this.getLastKnownLocation = getLastKnownLocation;
    }

    public void please() {
        Criteria criteria = new Criteria();
        List<String> providers = locationManager.getProviders(criteria, true);
        if (providers != null) {
            Location newestLocation = null;
            for (String provider : providers) {
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    if (newestLocation == null) {
                        newestLocation = location;
                    } else {
                        if (location.getTime() > newestLocation.getTime()) {
                            newestLocation = location;
                        }
                    }
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGPSProvider);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetworkProvider);
            }
            if (newestLocation != null) {
                listener.foundLocation(LocationManager.NETWORK_PROVIDER, newestLocation);
                return;

            }
            listener.couldntFindLocation();
        }
    }
    final LocationListener locationListenerNetworkProvider = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            FindMe.this.onLocationChanged(this, LocationManager.NETWORK_PROVIDER, location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            FindMe.this.onProviderDisabled(provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            FindMe.this.onProviderEnabled(provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };

    final LocationListener locationListenerGPSProvider = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            FindMe.this.onLocationChanged(this, LocationManager.GPS_PROVIDER, location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            FindMe.this.onProviderDisabled(provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            FindMe.this.onProviderEnabled(provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    };

    private void onLocationChanged(LocationListener locationListener, String provider, Location location) {
        locationManager.removeUpdates(locationListener);
        listener.foundLocation(provider, location);
    }

    private void onProviderDisabled(String provider) {
        Log.d("FindMe", "Provider disabled - " + provider);
    }

    private void onProviderEnabled(String provider) {
        Log.d("FindMe", "Provider enabled - " + provider);
    }

}