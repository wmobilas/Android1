package build.agcy.test1.Meetings.MapHelpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import build.agcy.test1.R;

public class MeetingPopupAdapter implements InfoWindowAdapter {
    private final Context context;
    private final LayoutInflater inflater;
    private View popup = null;
    private String address = null;
    private LatLng lastCoords = null;

    public MeetingPopupAdapter(Context context) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {


        final View rootView = inflater.inflate(R.layout.marker_meeting_new, null);

        // Getting the position from the marker
        LatLng newCoords = marker.getPosition();//стой)мб вот это
        // Getting reference to the TextView to set latitude
        final TextView tv_lng = (TextView) rootView.findViewById(R.id.tv_lat);

        // Getting reference to the TextView to set longitude
        final TextView tv_lat = (TextView) rootView.findViewById(R.id.tv_lng);
        //TextView mAddress = (TextView) findViewById(R.id.tv_lng);


        // todo: все комментарии не в тему и закрытый код, который мусор тоже нужно убрать.
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
            new GetAddressTask(context) {


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

        lastCoords = newCoords;


        // Returning the view containing InfoWindow contents
        return rootView;
    }
}