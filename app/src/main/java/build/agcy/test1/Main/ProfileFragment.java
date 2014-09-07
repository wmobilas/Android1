package build.agcy.test1.Main;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.R;
public class ProfileFragment extends Fragment {


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView userNameView = (TextView) rootView.findViewById(R.id.user_name);
        userNameView.setText(EatWithMeApp.currentUser.username);
        TextView idView = (TextView) rootView.findViewById(R.id.id);
        idView.setText(EatWithMeApp.currentUser.id);
        TextView longitudeView = (TextView) rootView.findViewById(R.id.longitude);
        longitudeView.setText(EatWithMeApp.currentUser.longitude);
        TextView latitudeView = (TextView) rootView.findViewById(R.id.latitude);
        latitudeView.setText(EatWithMeApp.currentUser.latitude);

        return rootView;
    }



}
