package build.agcy.test1;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    Marker marker;
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
        setContentView(R.layout.map);
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

//        MyLocationListener.LocationResult locationResult = new MyLocationListener.LocationResult() {
//            @Override
//            public void gotLocation(Location location) {
//                //Got the location!
//            }
//        };
        MyLocationListener myLocation = new MyLocationListener(this);
        if (myLocation.canGetLocation()) {
            Log.d("Your Location", "latitude:" + myLocation.getLatitude() + ", longitude: " + myLocation.getLongitude());
            latitude=myLocation.getLatitude();
            longitude=myLocation.getLongitude();
        } else {
            // Can't get user's current location
            // stop executing code by return
            return;
        }
//        MyLocationListener myLocation = new MyLocationListener();
       // myLocation.getLocation(this, locationResult);
     //   latitude = myLocation.latitude;
     //   longitude = myLocation.longitude;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(16)
                .bearing(45)
                .tilt(30)
                .build();
//        marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("I am here!"));
        map.setMyLocationEnabled(false);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
        map.setOnInfoWindowClickListener(this);
        map.setBuildingsEnabled(true);
        map.setOnInfoWindowClickListener(this);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
        //   map.setClustering(new ClusteringSettings().enabled(false).addMarkersDynamically(true));
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                if((myMarker!=null)
                          && (Math.abs(myMarker.getPosition().latitude - latLng.latitude) < 0.05
                          && Math.abs(myMarker.getPosition().longitude - latLng.longitude) < 0.05)) {
                    myMarker.setVisible(false);
                }
            }
        });
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            public void addMyMarker(LatLng latLng){

                myMarker = map.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .title("Hello world")
                        .snippet("This is my spot!")
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                //.fromResource(R.drawable.pin))

//                map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//                    @Override
//                    public View getInfoWindow(Marker marker) {
////
//                        TextView tv = new TextView(MapActivity.this);
//                        tv.setText("Test getInfoContents");
//                        return tv;
//                    }
//
//                    @Override
//                    public View getInfoContents(Marker marker) {
////                        if (marker.getId().equals(MapActivity.this.marker.getId())) {
//                            TextView tv = new TextView(MapActivity.this);
//                            tv.setText("Test getInfoWindow");
//                            tv.setTextColor(Color.RED);
//                            return tv;
////                        } else
////                            return null;
//                    }
//                });

            }
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (myMarker != null) {

                    myMarker.setVisible(false);
                }
                currentPosition = new LatLng(latLng.latitude, latLng.longitude);
                addMyMarker(latLng);
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
                .zoom(16)
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


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.mapfragment, container, false);
            return rootView;
        }
    }
}