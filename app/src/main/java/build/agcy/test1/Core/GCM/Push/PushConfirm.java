package build.agcy.test1.Core.GCM.Push;

import android.app.PendingIntent;
import android.content.Intent;

import build.agcy.test1.Meetings.MeetingActivity;

/**
 * Created by Freeman on 08.09.2014.
 */
public class PushConfirm extends Push {
    public String meetingId;

    public String getNotificationTitle() {
        return "New Confirm";
    }


    @Override
    public String getTitle() {
        return "You have been accepted to the meeting";
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    protected int getNotificationId() {
        return CONFIRM_NOTIFICATION;
    }

    @Override
    protected PendingIntent getPendingIntent() {
        Intent i = new Intent(context, MeetingActivity.class);
        i.putExtra("id", meetingId);
        return PendingIntent.getActivity(context, 0, i, 0);
    }
}

