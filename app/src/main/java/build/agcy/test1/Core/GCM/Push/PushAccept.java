package build.agcy.test1.Core.GCM.Push;

import android.app.PendingIntent;
import android.content.Intent;

import build.agcy.test1.Meetings.MeetingActivity;

/**
 * Created by Freeman on 08.09.2014.
 */
public class PushAccept extends Push {
    public String meetingId;
    public String id;
    public String message;


    @Override
    public String getTitle() {
        return "You have a new request to your meeting";
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    protected int getNotificationId() {
        return ACCEPT_NOTIFICATION;
    }

    @Override
    protected PendingIntent getPendingIntent() {
        Intent i = new Intent(context, MeetingActivity.class);
        i.putExtra("id", meetingId);
        return PendingIntent.getActivity(context, 0, i, 0);
    }
}
