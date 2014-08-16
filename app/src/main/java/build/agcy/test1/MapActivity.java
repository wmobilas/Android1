package build.agcy.test1;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
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

        final MyLocationListener myLocationService = new MyLocationListener(this){

        };
//        marker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("I am here!"));
        map.setMyLocationEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
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
        //   map.setClustering(new ClusteringSettings().enabled(false).addMarkersDynamically(true));
//        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
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
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            public String address = null;
            public LatLng lastCoords = null;

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(final Marker marker) {
                // Getting view from the layout file info_window_layout
                final View rootView = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                // Getting the position from the marker
                LatLng newCoords = marker.getPosition();//стой)мб вот это
                // Getting reference to the TextView to set latitude
                final TextView tv_lng = (TextView) rootView.findViewById(R.id.tv_lat);

                // Getting reference to the TextView to set longitude
                final TextView tv_lat = (TextView) rootView.findViewById(R.id.tv_lng);
                //TextView mAddress = (TextView) findViewById(R.id.tv_lng);

                //ImageView image = (ImageView) findViewById(R.id.info_window_icon);
                // image.setVisibility(View.VISIBLE);
                // Display the results of the lookup.
                //}
                // Setting the latitude
                // сюда суем объект того класса, и в doInBackground суем вот это то не надо? ддо этого работало?показывало координаты а как ты поменял вью?класс
                // как бы тебе объяснить... Короче когда мы берём маркер, то у нас должны быть новые координаты ( второе условие ). если же координаты одни и те же,
                // значт это мы повторили вызов маркера. Раз повторили вызов, то скорее всего адрес уже не нуль, поэтому мы биндим его. андроид иногда такой сложный -_- угу
                // поверь, записная книжка и синхронизация куда сложнее. это я тебе за 20 минут разобрался, там я ебал мозг неделю.ужс, а что такое записная книга... эм...
                // записная книгка андроидовская, там же контакты, всякие номера, имейлы, имена, там связываются эти контакты, если система видит, что одинаковые имена\телефоны\имейлы
                // тоже синхронизации, там пиздос) а зачем тебе понадобились контакты? надо было) ок для проекта того? да.эх) спасиб!)
                if (address == null || !newCoords.equals(lastCoords)) {
//                    GeoPoint newCurrent = new GeoPoint(59529200, 18071400);
                    Location current = new Location("reverseGeocoded");
                    current.setLatitude(marker.getPosition().latitude);
                    current.setLongitude(marker.getPosition().longitude);
                    current.setAccuracy(3333);
                    current.setBearing(333);
                    Location loc = current;

                    // myLocationService.getLocation(); // это не та лок, кстати. нужно как-то подругому из маркера брать
                    // Set activity indicator visibility to "gone"


                    final ProgressBar mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.address_progress);

//                    try{
//                        mActivityIndicator.post(new Runnable() {
//                            public void run() {
//                                mActivityIndicator.requestFocus();
                                mActivityIndicator.setVisibility(View.VISIBLE); // Or: btn.setVisibility(View.VISIBLE)
//                            }});
//                    }catch (Exception e) {
//                        Log.d("test", e.toString());
//
//                    }
                    //tv_lat.setText("Loading adress");
                    new GetAddressTask(getBaseContext()) {


                        @Override
                        protected void onPostExecute(final String s) {
                            address = s;

                            marker.showInfoWindow(); // повторный вызов маркера, потому что иначе он не обновляется.

                        }
                    }.execute(loc);
                } else {
                    final ProgressBar mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.address_progress);
//                        try {
//                            mActivityIndicator.post(new Runnable() {
//                                public void run() {
//                                    mActivityIndicator.requestFocus();
                                    mActivityIndicator.setVisibility(View.GONE); // Or: btn.setVisibility(View.VISIBLE)
//                                }
//                            });
//                        } catch (Exception e) {
//                            Log.d("test", e.toString());
//
//                        }


                    if (!address.equals("No address found")) {
                        String[] adresses = address.split(",");
                        String adres = "";
                        if ((!adresses[0].equals(" null")) && (!adresses[0].equals("")))
                            adres += "Street:" + adresses[0];
                        if ((!adresses[1].equals(" null")) && (!adresses[1].equals("")))
                            adres += " City:" + adresses[1];
                        if ((!adresses[2].equals(" null")) && (!adresses[2].equals("")))
                            adres += " Country:" + adresses[2];
                        tv_lat.setText(adres);
                        // Setting the longitude почему не ставится н мб чтото с лейаутом
                        tv_lng.setText("Longitude:" + lastCoords.longitude + " Lattitude:" + lastCoords.latitude);
                    }
                }

            lastCoords=newCoords;


            // Returning the view containing InfoWindow contents
            return rootView;

        }
        });

        // Adding and showing marker while touching the GoogleMap
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
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
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

//            public void addMyMarker(LatLng latlng){
//                myMarker = map.addMarker(new MarkerOptions()
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