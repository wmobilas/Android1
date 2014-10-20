package com.agcy.eatwithme.Meetings.MapHelpers;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agcy.eatwithme.R;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

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


    //    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    //    @Override
    public View getInfoWindow(final Marker marker) {


        final View rootView = inflater.inflate(R.layout.marker_meeting_new, null);

        // Getting the position from the marker
        LatLng newCoords = marker.getPosition();
        final TextView coordinatesField = (TextView) rootView.findViewById(R.id.popup_snippet);

        final TextView adressField = (TextView) rootView.findViewById(R.id.popup_title);
        if (address == null || !newCoords.equals(lastCoords)) {
            Location current = new Location("reverseGeocoded");
            current.setLatitude(marker.getPosition().latitude);
            current.setLongitude(marker.getPosition().longitude);
            current.setAccuracy(3333);
            current.setBearing(333);
            Location loc = current;
            final ProgressBar mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.address_progress);
            mActivityIndicator.setVisibility(View.VISIBLE);
            final ImageView img = (ImageView) rootView.findViewById(R.id.info_window_icon);
            img.setVisibility(View.GONE);
            adressField.setVisibility(View.GONE);
            coordinatesField.setVisibility(View.GONE);
            new GetAddressTask(context) {

                @Override
                protected void onPostExecute(final String s) {
                    address = s;
                    marker.showInfoWindow();
                }
            }.execute(loc);
        } else {
            final ProgressBar mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.address_progress);
            mActivityIndicator.setVisibility(View.GONE);
            final ImageView img = (ImageView) rootView.findViewById(R.id.info_window_icon);
            img.setVisibility(View.VISIBLE);
            adressField.setVisibility(View.VISIBLE);
            coordinatesField.setVisibility(View.VISIBLE);
            if (!address.equals("No address found")) {
                String[] adresses = address.split(",");
                String adres = "";
                if ((!adresses[0].equals(" null")) && (!adresses[0].equals("")))
                    adres += "Street:" + adresses[0];
//                if ((!adresses[1].equals(" null")) && (!adresses[1].equals("")))
//                    adres += " City:" + adresses[1];
//                if ((!adresses[2].equals(" null")) && (!adresses[2].equals("")))
//                    adres += " Country:" + adresses[2]);
                adressField.setText(adres);
                coordinatesField.setText("Longitude:" + lastCoords.longitude + " Lattitude:" + lastCoords.latitude);
            }
        }

        lastCoords = newCoords;


        // Returning the view containing InfoWindow contents
        return rootView;
    }
}