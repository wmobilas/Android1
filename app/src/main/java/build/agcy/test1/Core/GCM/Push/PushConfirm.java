package build.agcy.test1.Core.GCM.Push;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import build.agcy.test1.Meetings.MeetingActivity;
import build.agcy.test1.R;

/**
 * Created by Freeman on 08.09.2014.
 */
public class PushConfirm extends Push {
    public String meetingId;

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

    @Override
    protected NotificationCompat.Builder buildr() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);
        Resources res = context.getResources();
        Bitmap img = BitmapFactory.decodeResource
                (res, R.drawable.ic_stat_w256h2561377940716185119eatfoodforkknifestreamline2);
        builder
                .setSmallIcon(R.drawable.ic_stat_location_place)
                .setLargeIcon(img)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(getTitle())
                .setContentText(getMessage()).build();
        PendingIntent contentIntent = getPendingIntent();
        if (contentIntent != null)
            builder.setContentIntent(contentIntent);
        return builder;
    }
}

