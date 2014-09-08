package build.agcy.test1.Core.GCM.Push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import build.agcy.test1.Meetings.MeetingActivity;
import build.agcy.test1.R;

/**
 * Created by Freeman on 08.09.2014.
 */
public class PushConfirm {
    public String meetingId;

    public PushConfirm(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getNotificationTitle() {
        return "New Confirm";
    }

    public void getNotification(Context c) {

        final int NOTIFICATION_ID = 1;
        final String TAG = "GCMIntentService";
        NotificationManager mNotificationManager;
        NotificationCompat.Builder builder;
        mNotificationManager = (NotificationManager)
                c.getSystemService(Context.NOTIFICATION_SERVICE);

        Context ctx = c.getApplicationContext();
        Intent i = new Intent(c, MeetingActivity.class);
//        i.putExtra("id", meetingId);
        i.putExtra("id", "1");
        PendingIntent contentIntent = PendingIntent.getActivity(c, 0, i, 0);


        Resources res = ctx.getResources();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(c)
                        .setSmallIcon(R.drawable.pin)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.doge))
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("PushConfirm")
                        .setContentText("Confirm");

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        return;
    }
}

