package build.agcy.test1.Main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.R;

public class ProfileFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);
        if (savedInstanceState != null) {
            // Restore last state
        } else {
            profileView = inflater.inflate(R.layout.fragment_profile, container, false);

            TextView userNameView = (TextView) profileView.findViewById(R.id.user_name);
            userNameView.setText(EatWithMeApp.currentUser.username);
            TextView idView = (TextView) profileView.findViewById(R.id.id);
            idView.setText(EatWithMeApp.currentUser.id);
            TextView longitudeView = (TextView) profileView.findViewById(R.id.longitude);
            longitudeView.setText(EatWithMeApp.currentUser.longitude);
            TextView latitudeView = (TextView) profileView.findViewById(R.id.latitude);
            latitudeView.setText(EatWithMeApp.currentUser.latitude);
        }
        return profileView;
    }


}
