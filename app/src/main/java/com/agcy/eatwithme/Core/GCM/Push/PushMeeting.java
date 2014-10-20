package com.agcy.eatwithme.Core.GCM.Push;

import android.app.PendingIntent;
import android.content.Intent;

import com.agcy.eatwithme.Meetings.MeetingActivity;

/**
 * Created by Freeman on 08.09.2014.
 */
public class PushMeeting extends Push {
    public String id;
    public String description;

    @Override
    protected PendingIntent getPendingIntent() {
        Intent i = new Intent(context, MeetingActivity.class);
        i.putExtra("id", id);
        return PendingIntent.getActivity(context, 0, i, 0);
    }

    @Override
    public String getTitle() {
        return "New meeting nearby!";
    }

    @Override
    public String getMessage() {
        return description;
    }

    @Override
    protected int getNotificationId() {
        return MEETING_NOTIFICATION;
    }
}
