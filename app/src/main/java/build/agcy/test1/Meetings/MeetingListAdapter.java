package build.agcy.test1.Meetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

/**
 * Created by Freeman on 17.08.2014.
 */
public class MeetingListAdapter extends BaseAdapter {
    private final ArrayList<Meeting> meetings;
    protected final Context context;

    public MeetingListAdapter(Context context, ArrayList<Meeting> meetings) {
        this.context = context;
        this.meetings = meetings;
    }

    @Override
    public int getCount() {
        return meetings.size();
    }

    @Override
    public Meeting getItem(int position) {
        return meetings.get(getCount() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return getCount() - position - 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //todo сделать динамическую загрузку
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup view;
        if (convertView == null) {
            view = (ViewGroup) inflater.inflate(R.layout.item_meeting, parent, false);
        } else {
            view = (ViewGroup) convertView;
        }
        final Meeting meeting = getItem(position);
        ((TextView) view.findViewById(R.id.description)).setText(meeting.description);
        TextView count = (TextView) view.findViewById(R.id.count);
        if (meeting.acceptsCount == 0) {
            count.setVisibility(View.INVISIBLE);
        } else {
            count.setVisibility(View.VISIBLE);
            count.setText(meeting.acceptsCount);
        }
        String ownerid = meeting.owner.username;
        String currentUserid = EatWithMeApp.currentUser.username;
        if (ownerid.equals(currentUserid)) {
            ((TextView) view.findViewById(R.id.user_name)).setText("by you");
        } else {
            ((TextView) view.findViewById(R.id.user_name)).setText("by " + ownerid);
        }
//        ImageView imageView = (ImageView) view.findViewById(R.id.meeting_photo);
//        String imageUrl = Converters.getStaticMapImageUrl(meeting.longitude, meeting.latitude, 100, 100, 15, "purple", "Here");
//        ImageLoader.getInstance().displayImage(imageUrl, (ImageView)view.findViewById(R.id.mapground));
        return view;
    }
}
