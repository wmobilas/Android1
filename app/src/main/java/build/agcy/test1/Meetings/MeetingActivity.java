package build.agcy.test1.Meetings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import build.agcy.test1.Api.Meetings.MeetingGetTask;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

public class MeetingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meeting, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private String meeting_id;
        public Meeting meeting;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_meeting, container, false);
            Activity activity = getActivity();
            if(activity!=null) {
                Intent intent = activity.getIntent();
                Bundle bundle = intent.getExtras();
                String id = bundle.getString("meeting_id");
                this.meeting_id = id;
            }else{
                meeting_id = null;
            }
            MeetingGetTask task = new MeetingGetTask(meeting_id) {


                @Override
                public void onSuccess(Meeting meeting) {
                }

                @Override
                public void onError(Exception exp) {
                    ((TextView) rootView.findViewById(R.id.meeting_status)).setText("Error");
                   }
            };
            //task.start();

            //meeting = new Meeting(){{ meeting_id = "1"; description = "Bulochki`s";creator = "Ivan";}};
            meeting=new Meeting("1", "description", "creator","33.333333","33.333333",2222);
            PlaceholderFragment.this.meeting = meeting;
            ((TextView) rootView.findViewById(R.id.meeting_description)).setText(meeting.description);
            ((TextView) rootView.findViewById(R.id.meeting_status)).setVisibility(View.GONE);
            return rootView;
        }
    }
//    public void showTimePicker(View v) {
//        DialogFragment newFragment = new TimePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "timePicker");
//
//    }
}
