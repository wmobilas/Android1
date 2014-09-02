package build.agcy.test1.Meetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

/**
 * Created by Freeman on 17.08.2014.
 */
public class MeetingListAdapter extends BaseAdapter {
    private final ArrayList<Meeting> meetings;
    private final Context context;
    public MeetingListAdapter(Context context, ArrayList<Meeting> meetings){
        this.context = context;
        this.meetings = meetings;
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
        TextView descView = (TextView) rootView.findViewById(R.id.description);
        TextView coordsView = (TextView) rootView.findViewById(R.id.meeting_coords);
        final TextView creatorNameTextView = (TextView) rootView.findViewById(R.id.creator);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.meeting_photo);

        final Meeting meeting = getItem(position);

        descView.setText(meeting.description);
        creatorNameTextView.setText(meeting.creator);
        coordsView.setText(meeting.latitude+"\n"+meeting.longitude);

        String imageUrl = ""+
        "http://maps.googleapis.com/maps/api/staticmap?zoom=12&size=100x100&maptype=roadmap" +
        "&markers=color:red%7Clabel:Here%7C"+
        meeting.longitude +","+
        meeting.latitude+"";
        ImageLoader.getInstance().displayImage(imageUrl, imageView);

        return rootView;
    }
}
