package build.agcy.test1.Core.Helpers;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import build.agcy.test1.Core.MyLocationListener;

public class FindMe {
    public static interface FindMeListener {
        public void foundLocation(String provider, Location location);

        public void couldntFindLocation();
    }

    public static FindMe please(Context context, int timeout, boolean getLastKnownLocation, FindMeListener listener) {
        FindMe findMe = new FindMe(context, timeout, getLastKnownLocation, listener);
        findMe.please(context);
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

    public void please(Context mContext) {
        boolean isGPSEnabled, isNetworkEnabled;
        final MyLocationListener myLocationService = new MyLocationListener(mContext) {
        };
        if (locationManager == null) {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);
        }

        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 100, myLocationService);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 100,
                myLocationService);
        locationManager.requestLocationUpdates(
                LocationManager.PASSIVE_PROVIDER, 0, 100,
                myLocationService);
        Location location;
        if ((locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) || (isGPSEnabled)) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            listener.foundLocation(LocationManager.GPS_PROVIDER, location);
        } else {
            if ((isNetworkEnabled) || (locationManager
                    .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null) || (locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)) {
                if (locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null) {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    listener.foundLocation(LocationManager.PASSIVE_PROVIDER, location);
                } else {
                    location = locationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    listener.foundLocation(LocationManager.NETWORK_PROVIDER, location);
                }
            }
        }
        listener.couldntFindLocation();
    }

}