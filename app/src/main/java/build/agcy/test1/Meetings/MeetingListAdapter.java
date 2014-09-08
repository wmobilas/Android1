package build.agcy.test1.Meetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import build.agcy.test1.Core.Helpers.Converters;
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
        return meetings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_meeting, null);
        TextView descView = (TextView) itemView.findViewById(R.id.description);
        final TextView userNameTextView = (TextView) itemView.findViewById(R.id.user_name);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.meeting_photo);

        final Meeting meeting = getItem(position);
        userNameTextView.setText(meeting.owner.username);

        descView.setText(meeting.description);

        String imageUrl = Converters.getStaticMapImageUrl(meeting.longitude, meeting.latitude, 600, 400, 7, "red", "Here");
        // ImageLoader.getInstance().displayImage(imageUrl, imageView);

        return itemView;
    }
}
