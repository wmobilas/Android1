package build.agcy.test1.Meetings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

public class MeetingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container1, new PlaceholderMeetingFragment())
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

    public static class PlaceholderMeetingFragment extends Fragment {

        public PlaceholderMeetingFragment() {
        }
        public static View meeting_activity_View;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (meeting_activity_View != null) {
                ViewGroup parent = (ViewGroup) meeting_activity_View.getParent();
                if (parent != null)
                    parent.removeView(meeting_activity_View);
            }
            try{
                meeting_activity_View = inflater.inflate(R.layout.fragment_meeting, container, false);
            } catch (InflateException e) {
            }
            Activity activity = getActivity();
            if(activity!=null) {
                Intent intent = activity.getIntent();
                Bundle bundle = intent.getExtras();
                ((TextView) meeting_activity_View.findViewById(R.id.meeting_creator)).setText("by " + bundle.getString("meeting_creator"));
                ((TextView) meeting_activity_View.findViewById(R.id.meeting_description1)).setText(bundle.getString("meeting_description"));
                (meeting_activity_View.findViewById(R.id.meeting_status)).setVisibility(View.GONE);
            }else{
                Meeting meeting=new Meeting("1", "description", "creator","33.333333","33.333333",2222);
                ((TextView) meeting_activity_View.findViewById(R.id.meeting_description1)).setText(meeting.description);
                ((TextView) meeting_activity_View.findViewById(R.id.meeting_creator)).setText("by "+meeting.creator);
                (meeting_activity_View.findViewById(R.id.meeting_status)).setVisibility(View.GONE);
            }
            return meeting_activity_View;
        }
    }
}
