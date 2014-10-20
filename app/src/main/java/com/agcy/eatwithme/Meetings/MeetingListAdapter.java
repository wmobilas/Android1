package com.agcy.eatwithme.Meetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agcy.eatwithme.EatWithMeApp;
import com.agcy.eatwithme.Models.Meeting;
import com.agcy.eatwithme.R;

import java.util.ArrayList;

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
        return position;
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
        ProgressBar loading = (ProgressBar) view.findViewById(R.id.meeting_loading);
        view.findViewById(R.id.relativeLayout).setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
        final Meeting meeting = getItem(position);
        ((TextView) view.findViewById(R.id.description)).setText(meeting.description);
        TextView count = (TextView) view.findViewById(R.id.count);
        if (meeting.acceptsCount == 0) {
            count.setVisibility(View.INVISIBLE);
        } else {
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(meeting.acceptsCount));
        }
        String ownerid = meeting.owner.username;
        String currentUserid = EatWithMeApp.currentUser.username;
        if (ownerid.equals(currentUserid)) {
            ((TextView) view.findViewById(R.id.user_name)).setText("By you");
        } else {
            ((TextView) view.findViewById(R.id.user_name)).setText("By " + ownerid);
        }
        view.findViewById(R.id.relativeLayout).setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
//        ImageView imageView = (ImageView) view.findViewById(R.id.meeting_photo);
//        String imageUrl = Converters.getStaticMapImageUrl(meeting.longitude, meeting.latitude, 100, 100, 15, "green", "Here");
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheOnDisk(true)
//                .cacheInMemory(true)
//                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//                .build();
//        ImageLoader.getInstance().displayImage(imageUrl, imageView, options);
        return view;
    }
}