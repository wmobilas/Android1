package build.agcy.test1.Meetings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import build.agcy.test1.Api.Meetings.MeetingListTask;
import build.agcy.test1.Chat.AndyChatActivity;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

/**
 * Created by Freeman on 15.08.2014.
 */

public class MeetingsListActivity extends Activity {

    ArrayList<Meeting> meetings = new ArrayList<Meeting>();
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
        getMenuInflater().inflate(R.menu.user_list, menu);
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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_meeting_list,container, false);
            final ListView listView = (ListView) rootView.findViewById(R.id.meeting_list);
            MeetingListTask task = new MeetingListTask(new ArrayList<NameValuePair>()) {
                @Override
                public void onSuccess(Meeting[] response) {
//                    listView.setAdapter(new UserAdapter(getActivity(), new ArrayList<User>(response)));
                    // короче как-то биндим тоже тут, когда будет работать апи
                }

                @Override
                public void onError(Exception exp) {
                    // Toast

                }
            };
            listView.setAdapter(new MeetingAdapter(getActivity(), null));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    bundle.putString("meeting_id", "1");
                    Intent intent= new Intent(getActivity(), MeetingActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return rootView;
        }
    }
    public void mapOpen(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        Meeting meeting = new Meeting("1", "description", "creator","33.333333","33.333333",2222);
//        meeting.meeting_id ="1";
//        meeting.creator="Petr";
//        meeting.description="Baton`s";
//        meeting.longitude="36.077560";
//        meeting.latitude="36.077560";
//        meeting.time=1111;
        meetings.add(meeting);
       intent.putParcelableArrayListExtra("meetings",meetings);
      startActivity(intent);
   }
}
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_meeting_list);
//
//        runOnUiThread(new Runnable() {
//            public void run() {
//                ArrayList<HashMap<String, String>> meetingsList = new ArrayList<HashMap<String, String>>();
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("textreference", "peopleference");
//                map.put("textname", "peoplename");
//                meetingsList.add(map);
//                meetingsList.add(map);
//                ListView lv = (ListView) findViewById(R.id.meeting_list);
//                ListAdapter adapter = new SimpleAdapter(MeetingsListActivity.this, meetingsList,
//                        R.layout.item_meeting,
//                        new String[]{"textreference", "textname"}, new int[]{
//                        R.id.meeting_creator, R.id.meeting_description});
//
//                lv.setAdapter(adapter);
//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        startActivity(new Intent(getBaseContext(), MeetingActivity.class));
//                    }
//                });
//                // todo: open an MeetingActivity normally
//                // Adding data into listview прежде чем перенаправлять на активити
//                // ну я еще не умеюнастроек нужно сделать диалог, что у юзера отключен гпс
//            }
//        });
//    }
//}
