package build.agcy.test1.Meetings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import build.agcy.test1.Api.Meetings.MeetingAcceptTask;
import build.agcy.test1.EatWithMeApp;
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
            final SharedPreferences prefs = getActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
            Activity activity = getActivity();
            if(activity!=null) {
                Intent intent = activity.getIntent();
                Bundle bundle = intent.getExtras();
                ((TextView) meeting_activity_View.findViewById(R.id.meeting_creator)).setText("by " + bundle.getString("meeting_creator"));
                ((TextView) meeting_activity_View.findViewById(R.id.meeting_description1)).setText(bundle.getString("meeting_description"));
                (meeting_activity_View.findViewById(R.id.meeting_status)).setVisibility(View.GONE);
                prefs.edit().putString("meeting_selected_id",bundle.getString("meeting_id")).commit();
            }else{
                Meeting meeting=new Meeting("1", "description", "creator","33.333333","33.333333",2222);
                ((TextView) meeting_activity_View.findViewById(R.id.meeting_description1)).setText(meeting.description);
                ((TextView) meeting_activity_View.findViewById(R.id.meeting_creator)).setText("by "+meeting.creator);
                (meeting_activity_View.findViewById(R.id.meeting_status)).setVisibility(View.GONE);
            }
            if(prefs.contains("user_meeting_id")) {
                if ((prefs.getString("meeting_selected_creator", "meeting_selected_creator").equals(EatWithMeApp.currentUser.username))) {
                    (meeting_activity_View.findViewById(R.id.user_accepted_list)).setVisibility(View.VISIBLE);
                    (meeting_activity_View.findViewById(R.id.user_accepted_list_status)).setVisibility(View.VISIBLE);
                    (meeting_activity_View.findViewById(R.id.accept_button)).setVisibility(View.GONE);
                    //todo добавить сортировку
                }// если создана встреча и ее открывают
            }else {// если встреча не создана и открывают одну из списка
                (meeting_activity_View.findViewById(R.id.user_accepted_list)).setVisibility(View.GONE);
                (meeting_activity_View.findViewById(R.id.user_accepted_list_status)).setVisibility(View.GONE);
                (meeting_activity_View.findViewById(R.id.accept_button)).setVisibility(View.VISIBLE);
                (meeting_activity_View.findViewById(R.id.accept_button)).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String message = ((TextView) meeting_activity_View.findViewById(R.id.accept_message)).getText().toString();
                                if (message == null || message.equals("")) {
                                    message = "Hello!";
                                }
                                MeetingAcceptTask task = new MeetingAcceptTask(prefs.getString("meeting_selected_id", "id"), message) {
                                    @Override
                                    public void onSuccess(String response) {
                                        Toast.makeText(getActivity().getApplicationContext(), "MeetinActivityAcceptSuccess ", Toast.LENGTH_LONG).show();
                                        Log.d("agcy.test1", "MeetinActivityAcceptSuccess");

                                    }

                                    @Override
                                    public void onError(Exception exp) {
                                        Toast.makeText(getActivity().getApplicationContext(), "MeetingListTaskError " + exp.toString(), Toast.LENGTH_LONG).show();
                                        Log.d("agcy.test1", "MeetinActivityAcceptError " + exp.toString());
                                    }
                                };
                                task.start();
                            }
                        });
            }

            return meeting_activity_View;
        }
    }
}
