package build.agcy.test1.Main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;

import build.agcy.test1.Api.Meetings.MeetingHistoryTask;
import build.agcy.test1.Core.Helpers.Converters;
import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Meetings.MeetingActivity;
import build.agcy.test1.Meetings.MeetingHistoryAdapter;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

public class ProfileFragment extends Fragment {


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {

        } else {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            TextView userNameView = (TextView) rootView.findViewById(R.id.user_name);
            userNameView.setText(EatWithMeApp.currentUser.username);
            ImageView backgroundView = (ImageView) rootView.findViewById(R.id.background);
            String backgroundUrl = Converters.getStaticMapImageUrl(
                    EatWithMeApp.currentUser.longitude,
                    EatWithMeApp.currentUser.latitude,
                    640, 640,
                    7);
            ImageLoader.getInstance().displayImage(backgroundUrl, backgroundView);
            ImageView photoView = (ImageView) rootView.findViewById(R.id.photo);
            if (EatWithMeApp.currentUser.photo != null) {
                ImageLoader.getInstance().displayImage(EatWithMeApp.currentUser.photo, photoView);
            }
            final ListView meetingListView = (ListView) rootView.findViewById(R.id.history_list);
            MeetingHistoryTask task = new MeetingHistoryTask() {
                @Override
                public void onSuccess(final Meeting[] response) {
                    if (response.length == 0) {
                        ((TextView) rootView.findViewById(R.id.history_text)).setText("You have no meetings yet");
                        (rootView.findViewById(R.id.history_status)).setVisibility(View.VISIBLE);
                    } else
                        rootView.findViewById(R.id.history_status).setVisibility(View.GONE);
                    final MeetingHistoryAdapter adapter = new MeetingHistoryAdapter(getActivity(), new ArrayList<Meeting>(Arrays.asList(response)));
                    meetingListView.setAdapter(adapter);
                    meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", String.valueOf(response[position].id));
                            Intent intent = new Intent(getActivity(), MeetingActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onError(Exception exp) {
                    Toast.makeText(getActivity().getApplicationContext(), "MeetingListTaskError " + exp.toString(), Toast.LENGTH_LONG).show();
                }
            };
            task.start();
        }
        return rootView;
    }


}
