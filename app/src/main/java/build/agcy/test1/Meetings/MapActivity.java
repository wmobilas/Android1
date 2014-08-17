package build.agcy.test1.Meetings;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import build.agcy.test1.Meetings.MapHelpers.MeetingPopupAdapter;
import build.agcy.test1.Core.MyLocationListener;
import build.agcy.test1.R;

/**
 * Created by Freeman on 14.08.2014.
 */
public class MapActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener {
    SupportMapFragment mapFragment;
    GoogleMap map;
    final String TAG = "myLogs";
    double latitude;
    double longitude;
    LatLng currentPosition;
    Marker myMarker;
    LocationManager locationManager;
    String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // MyLocationListener.SetUpLocationListener(this);
//        final Context mainContext = this;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                MyLocationListener.SetUpLocationListener(mainContext);
//            }
//        }).start();
      //  LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
      //  Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        map = mapFragment.getMap();

        if (map == null) {
            finish();
            return;
        }
        init();
    }
    private void init() {

        final MyLocationListener myLocationService = new MyLocationListener(this){

        };
//        marker = activity_map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("I am here!"));
        map.setMyLocationEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setInfoWindowAdapter(new MeetingPopupAdapter(getBaseContext()));
        map.setOnInfoWindowClickListener(this);
        map.setBuildingsEnabled(true);
        map.setOnInfoWindowClickListener(this);


//        if (myLocationService.canGetLocation()) {
//            Log.d("Your Location", "latitude:" + myLocationService.getLatitude() + ", longitude: " + myLocationService.getLongitude());
//            latitude=myLocationService.getLatitude();
//            longitude=myLocationService.getLongitude();
//        } else {
            // Can't get user's current location
            // stop executing code by return
//            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//            Location location = null;
//            if (lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!=null){
//                location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            }
//            else{
//                if (lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)!=null){
//                    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                }
//                else {
//                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(intent);
//                    }
//
//            }}

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Define the criteria how to select the locatioin provider -> use
            // default
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
        Location location = null;
        if (locationManager.getLastKnownLocation(provider)!=null){
                    location = locationManager.getLastKnownLocation(provider);

            latitude=location.getLatitude();
            longitude=location.getLongitude();
                }
                else {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
            latitude=myLocationService.getLatitude();
            longitude=myLocationService.getLongitude();
            Toast.makeText(getApplicationContext(), "Please Turn GPS On",Toast.LENGTH_LONG );
                    }
            //return;


        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(16)
                .bearing(45)
                .tilt(30)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
        //   activity_map.setClustering(new ClusteringSettings().enabled(false).addMarkersDynamically(true));
//        activity_map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//
//            @Override
//            public void onMapClick(LatLng latLng) {
//                if((myMarker!=null)
//                          && (Math.abs(myMarker.getPosition().latitude - latLng.latitude) < 0.05
//                          && Math.abs(myMarker.getPosition().longitude - latLng.longitude) < 0.05)) {
//                    myMarker.setVisible(false);
//                }
//            }
//        });
        map.setInfoWindowAdapter(
                new MeetingPopupAdapter(getBaseContext()));


        // Adding and showing marker while touching the GoogleMap
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {
                // Clears any existing markers from the GoogleMap
                map.clear();

                // Creating an instance of MarkerOptions to set position
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting position on the MarkerOptions
                markerOptions.position(position);

                // Animating to the currently touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(position));

                // Adding marker on the GoogleMap
                Marker marker = map.addMarker(markerOptions);

                // Showing InfoWindow on the GoogleMap
                marker.showInfoWindow();


            }
        });
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

//            public void addMyMarker(LatLng latlng){
//                myMarker = activity_map.addMarker(new MarkerOptions()
//                        .position(currentPosition)
//                        .title("Hello world")
//                        .snippet("This is my spot!")
//                        .draggable(true)
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                //.fromResource(R.drawable.pin))
//
////
//            }
            @Override
            public void onMapLongClick(LatLng arg0) {

//                if (myMarker != null) {
//
//                    myMarker.setVisible(false);
//                }
//                currentPosition = new LatLng(latLng.latitude, latLng.longitude);
//                addMyMarker(latLng);

                // Clears any existing markers from the GoogleMap
                map.clear();

                // Creating an instance of MarkerOptions to set position
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting position on the MarkerOptions
                markerOptions.position(arg0);

                // Animating to the currently touched position
                map.animateCamera(CameraUpdateFactory.newLatLng(arg0));

                // Adding marker on the GoogleMap
                Marker marker = map.addMarker(markerOptions);

                // Showing InfoWindow on the GoogleMap
                marker.showInfoWindow();

            }
        });

        map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition camera) {
                Log.d(TAG, "onCameraChange: " + camera.target.latitude + "," + camera.target.longitude);
            }
        });
    }

    public void onClickTest(View view) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(12)
                .bearing(45)
                .tilt(30)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tabbd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        MapMarkers markInfo = markerInfo.get(marker);
//
//        Intent i = new Intent(MapActivity.this,
//                MarkerInformation.class);
//        i.putExtra("name", markInfo.getTitle()).putExtra(
//                "description", markInfo.getDesc());
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);

    }


}