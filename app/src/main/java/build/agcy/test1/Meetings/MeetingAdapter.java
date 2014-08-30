package build.agcy.test1.Meetings;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

/**
 * Created by Freeman on 17.08.2014.
 */
public class MeetingAdapter extends BaseAdapter {
    private final ArrayList<Meeting> meetings;
    private final Context context;
    public MeetingAdapter(Context context, ArrayList<Meeting> meetings){
        this.context = context;
        this.meetings = meetings;
//                new ArrayList<Meeting>(){{
////            add(new Meeting(){{
////                meeting_id ="1";
////                creator="Petr";
////                description="Baton`s";
////                longitude="36.077560";
////                latitude="36.077560";
////                time=1;
////            }});
////            add(new Meeting(){{
////                meeting_id ="2";
////                creator="Ivan";
////                description="Bulk`s";
////                longitude="35.077560";
////                latitude="35.077560";
////                time=1;
////            }});
//            add(new Meeting("1", "description", "creator","33.333333","33.333333",1111));
//            add(new Meeting("2", "description2", "creator2","34.333333","34.333333",2222));
//        }};
    }
    @Override
    public int getCount() {
        return meetings.size();
    }

    @Override
    public Meeting getItem(int position) {
        return meetings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.item_meeting, null);
        TextView descView = (TextView) rootView.findViewById(R.id.meeting_description);
        final TextView creator = (TextView) rootView.findViewById(R.id.meeting_creator);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.meeting_photo);
        final Meeting meeting = getItem(position);
        descView.setText(meeting.description);
        // imageView и биндим как-то картинку, не важно
//        UserTask task = new UserTask(meeting.creator) {
//            @Override
//            public void onSuccess(User user) {
//                creator.setText(user.username);
//            }
//            @Override
//            public void onError(Exception exp) {
//                creator.setText(meeting.creator);
//                Toast.makeText(context, "MeetingListUserTaskError " + exp.toString(), Toast.LENGTH_LONG).show();
//            }
//        };
//        task.start();
        SharedPreferences prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        creator.setText(
                prefs.getString(meeting.creator, "creator_id"));
        return rootView;
    }
}
