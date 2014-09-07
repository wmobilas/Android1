package build.agcy.test1.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.List;

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
    LatLng currentPosition;
    Marker myMarker;
    LocationManager locationManager;
    String provider;
    private Activity myContext;

    public MapFragment() {
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        Log.d(TAG, "mapfragment onAttach");
        myContext = activity;
    }

    private static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.activity_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        final Button testButtonMap = (Button) view.findViewById(R.id.btnTest);
        testButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });

        return view;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {

//        GoogleMapOptions options = new GoogleMapOptions().camera(CameraPosition.fromLatLngZoom(new LatLng(37.4005502611301, -5.98233461380005), 16))
//                .compassEnabled(false).mapType(GoogleMap.MAP_TYPE_NORMAL).rotateGesturesEnabled(false).scrollGesturesEnabled(false).tiltGesturesEnabled(false)
//                .zoomControlsEnabled(false).zoomGesturesEnabled(false);
//        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment == null) {
//            mapFragment = SupportMapFragment.newInstance(options);
//            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.map, mapFragment);
//            fragmentTransaction.apply();
//        }
        super.onCreate(savedInstanceState);
        Log.d(TAG, "mapfragment onCreate");
        FragmentManager fm = getFragmentManager();
        mapFragment = (com.google.android.gms.maps.MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = com.google.android.gms.maps.MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
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

    private void init() {

        if (count == 0) {
        final MyLocationListener myLocationService = new MyLocationListener(myContext) {

        };
//        marker = activity_map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("I am here!"));
        map.setMyLocationEnabled(true);
        Log.d(TAG, "map!=null!!!!");
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setInfoWindowAdapter(new MeetingPopupAdapter(myContext.getBaseContext()));
        map.setOnInfoWindowClickListener(this);
        //map.setBuildingsEnabled(true);

        locationManager = (LocationManager) myContext.getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default

            Criteria criteria = new Criteria();
            latitude = myLocationService.getLatitude();
            longitude = myLocationService.getLongitude();
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
                        locationManager.requestLocationUpdates(provider, 0, 0, myLocationService);
                    }
                }
                if (newestLocation != null) {
                    longitude = newestLocation.getLongitude();
                    latitude = newestLocation.getLatitude();
                }
            } else {
                LocationDialogFragment dialog = new LocationDialogFragment();
                dialog.show(getFragmentManager(),
                        LocationDialogFragment.class.getName());
            }
//        Criteria criteria = new Criteria();
//            criteria.setAccuracy(Criteria.ACCURACY_FINE);
//            provider = locationManager.getBestProvider(criteria, true);
//            if (locationManager.getLastKnownLocation(provider) == null) {
//                if (locationManager.getLastKnownLocation(provider) == null) {
//                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                }
//            }
//            Location location = locationManager.getLastKnownLocation(provider);
//            if (location != null) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                return;
//            } else {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//            Toast.makeText(myContext.getApplicationContext(), "Please Turn GPS On", Toast.LENGTH_LONG).show();

            addMeetings(map);
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
        map.setInfoWindowAdapter(
                new MeetingPopupAdapter(myContext.getBaseContext()));
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
        count++;
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


    //    private void addMeetings(MeetingAdapter meetings, GoogleMap map){
//        MeetingAdapter meetingsList = meetings;
//        ArrayList<Marker> markers = new ArrayList<Marker>();
//        int meeting_length = meetings.getCount();
//        for (int i=0;i<meeting_length;i++){
//            int meeting_time = meetingsList.getItem(i).time;
//            String meeting_name=meetingsList.getItem(i).description;
//            String meeting_creator=meetingsList.getItem(i).creator;
//            double parseLat=Double.parseDouble(meetingsList.getItem(i).latitude);
//            double parseLng=Double.parseDouble(meetingsList.getItem(i).longitude);
//
//            LatLng cordinats = new LatLng(parseLat, parseLng);
//            Marker marker = map.addMarker(new MarkerOptions()
//                .position(cordinats)
//                .title(meeting_name)
//                .snippet(meeting_time +" " + meeting_creator)
//                .icon(BitmapDescriptorFactory
//                        .fromResource(R.drawable.pin)));
//        markers.add(marker);}
//    }
    private void addMeetings(final GoogleMap map) {
        final MeetingListTask task = new MeetingListTask(new ArrayList<NameValuePair>()) {
            @Override
            public void onSuccess(final Meeting[] response) {
                for (Meeting m : response) {
                    map.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(m.latitude), Double.parseDouble(m.longitude)))
                                    .title(m.description)
                                    .snippet(m.time + " " + m.Id)
//                            .icon(BitmapDescriptorFactory
//                                    .fromResource(R.drawable.pin))
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
                .image(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                .anchor(0, 1)
                .position(cordination, 50f, 50f));


        mGroundOverlay.setTransparency(0.1f);

    }
}