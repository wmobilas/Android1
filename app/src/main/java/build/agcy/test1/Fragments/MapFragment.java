package build.agcy.test1.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;

import java.lang.reflect.Field;
import java.util.ArrayList;

import build.agcy.test1.Api.Meetings.MeetingListTask;
import build.agcy.test1.Api.UpdateLocationTask;
import build.agcy.test1.Core.MyLocationListener;
import build.agcy.test1.Meetings.MapHelpers.MeetingPopupAdapter;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

/**
 * Created by Freeman on 14.08.2014.
 */
public class MapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {
    private com.google.android.gms.maps.MapFragment mapFragment;
    private GoogleMap map;
    final String TAG = "agcy.test";
    double latitude;
    double longitude;
    int count = 0;
    LocationManager locationManager;
    private Activity myContext;
    private Location location;
    private boolean hasGpsProvider, hasNetworkProvider;

    public MapFragment() {
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        Log.d(TAG, "mapfragment onAttach");
        myContext = activity;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Fragment1 onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Fragment1 onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Fragment1 onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Fragment1 onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Fragment1 onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Fragment1 onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Fragment1 onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Fragment1 onDetach");
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static View mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.removeAllViews();
        container.removeAllViewsInLayout();
        if (mapView != null) {
            return mapView;
        }
        mapView = inflater.inflate(R.layout.activity_map, container, false);
        if (savedInstanceState != null) {
            // Restore last state
        }
//        else {
//            Button testButtonMap = (Button) mapView.findViewById(R.id.btnTest);
//            testButtonMap.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//                    CameraPosition cameraPosition = new CameraPosition.Builder()
//                            .target(new LatLng(latitude, longitude))
//                            .zoom(12)
//                            .bearing(45)
//                            .tilt(30)
//                            .build();
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
//                    map.animateCamera(cameraUpdate);
//
//                }
//            });
//        }
        return mapView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "mapfragment onCreate");
        FragmentManager fm = getFragmentManager();
        mapFragment = (com.google.android.gms.maps.MapFragment) fm.findFragmentById(R.id.map_Fragment);
        if (mapFragment == null) {
            mapFragment = com.google.android.gms.maps.MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_Fragment, mapFragment).commit();
        }
        setUpMapIfNeeded();

    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the
        // map.
        if (map != null) {
            init();
        } else {
            // Try to obtain the map from the SupportMapFragment.
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    if (map == null) {
                        // Try to obtain the map from the SupportMapFragment.
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                map = mapFragment.getMap();
                                init();
                            }
                        }, 1000);
                        map = mapFragment.getMap();
                        init();
                    }
                }
            }, 100);
            // Check if we were successful in obtaining the map.
        }

    }

    public Location getLocation(Context mContext) {

        final MyLocationListener myLocationService = new MyLocationListener(myContext) {
        };
        if (locationManager == null) {
            locationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);
        }

        hasGpsProvider = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        hasNetworkProvider = locationManager
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
                .getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) || (hasGpsProvider)) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            return location;
        } else {
            if ((hasNetworkProvider) || (locationManager
                    .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) != null)) {
                if (locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER) == null) {
                    return locationManager
                            .getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }
            }
        }
        return locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    private void init() {

        if (count == 0) {
//            final Location[] newestLocation = {null};

//        marker = activity_map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("I am here!"));
            map.setMyLocationEnabled(true);
            Log.d(TAG, "map!=null!!!!");
            map.getUiSettings().setAllGesturesEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    MapFragment.this.location = location;
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latitude, longitude))
                            .zoom(16)
                            .bearing(45)
                            .tilt(30)
                            .build();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                    map.animateCamera(cameraUpdate);
                }
            });
            if (location == null) {
                location = getLocation(myContext);
            }
            map.getUiSettings().setCompassEnabled(false);
            map.setInfoWindowAdapter(new MeetingPopupAdapter(myContext.getBaseContext()));
            map.setOnInfoWindowClickListener(this);
            //map.setBuildingsEnabled(true);

            if (location == null) {
                LocationDialogFragment dialog = new LocationDialogFragment();
                dialog.show(getFragmentManager(),
                        LocationDialogFragment.class.getName());
            } else {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
            addMeetings(map);
        }
        count++;
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(16)
                .bearing(45)
                .tilt(30)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
        UpdateLocationTask taskLocation = new UpdateLocationTask(
                String.valueOf(latitude),
                String.valueOf(longitude)) {
            @Override
            public void onSuccess(String response) {
            }

            @Override
            public void onError(Exception exp) {
            }
        };
        taskLocation.start();
//                    new MeetingPopupAdapter(myContext.getBaseContext()));
        map.setInfoWindowAdapter(
                new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        return null;
                    }
                });
        // Adding and showing marker while touching the GoogleMap
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {
                // Clears any existing markers from the GoogleMap
//                map.clear();

                // Creating an instance of MarkerOptions to set position
//                MarkerOptions markerOptions = new MarkerOptions();
//
//                // Setting position on the MarkerOptions
//                markerOptions.position(position);
//
//                // Animating to the currently touched position
//                map.animateCamera(CameraUpdateFactory.newLatLng(position));
//
//                // Adding marker on the GoogleMap
//                Marker marker = map.addMarker(markerOptions);
//
//                // Showing InfoWindow on the GoogleMap
//                marker.showInfoWindow();


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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.tabbd, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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

    private void addMyMarker(GoogleMap map, double lat, double lon,
                             int title, int snippet) {
        Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(getString(title))
                .snippet(getString(snippet)));

//        if (image != null) {
//            images.put(marker.getId(),
//                    Uri.parse("http://misc.commonsware.com/mapsv2/"
//                            + image));
    }


    private void addMeetings(final GoogleMap map) {
        final MeetingListTask task = new MeetingListTask(new ArrayList<NameValuePair>()) {
            @Override
            public void onSuccess(final Meeting[] response) {
                for (Meeting m : response) {
                    map.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(m.latitude), Double.parseDouble(m.longitude)))
                                    .title(m.description)
                                    .snippet(m.time + " " + m.id)
//                                    .icon(BitmapDescriptorFactory
//                                    .fromResource(R.drawable.pinb))
                    );
                }
            }

            @Override
            public void onError(Exception exp) {
                Toast.makeText(getActivity().getApplicationContext(), "MeetingListTaskError " + exp.toString(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "MeetingListTaskError " + exp.toString());
            }
        };
        task.start();

    }


    private void addGroundOverlay(GoogleMap map, double lat, double lng, Image image) {
        GroundOverlay mGroundOverlay;
        LatLng cordination = new LatLng(lat, lng);
        mGroundOverlay = map.addGroundOverlay(new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.pinb))
                .anchor(0, 1)
                .position(cordination, 50f, 50f));


        mGroundOverlay.setTransparency(0.1f);

    }
}