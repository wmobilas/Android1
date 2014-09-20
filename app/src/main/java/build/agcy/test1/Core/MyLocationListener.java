package build.agcy.test1.Core;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class MyLocationListener extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location = null; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public MyLocationListener(Context context) {
        this.mContext = context;
//        if (location == null) {
        location = getLocation();
//        updateLocation();
//        }
    }

    public Location getLocation() {

        boolean isGPSEnabled, isNetworkEnabled;
        if (locationManager == null) {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);
        }
        Boolean passiveNotEmpty = locationManager
                .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null;
        Boolean networkNotEmpty = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null;
        Boolean gpsNotEmpty = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER) != null;
        isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if ((!isGPSEnabled) && ((isNetworkEnabled) || (passiveNotEmpty) || (networkNotEmpty))) {
            if (networkNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                return location;
            }
            if (passiveNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                return location;
            }
            if (gpsNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return location;
            }
            return location;
        } else {
            if (gpsNotEmpty) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return location;
            }
        }
        if (networkNotEmpty) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return location;
        }
        if (passiveNotEmpty) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            return location;
        }

        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(MyLocationListener.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        this.location = location;
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

    public void updateLocation() {
        if (location == null) {
            location = getLocation();
        }
//        if (location != null) {
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//        }
    }
}