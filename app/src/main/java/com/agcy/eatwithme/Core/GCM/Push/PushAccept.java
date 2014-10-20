package com.agcy.eatwithme.Core.GCM.Push;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.agcy.eatwithme.Meetings.MeetingActivity;
import com.agcy.eatwithme.R;
import com.agcy.eatwithme.Users.UserActivity;

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

    protected PendingIntent getUserIntent() {
        Intent i = new Intent(context, UserActivity.class);
        i.putExtra("id", id);
        return PendingIntent.getActivity(context, 0, i, 0);
    }

    @Override
    protected NotificationCompat.Builder buildr() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);
        //
        PendingIntent contentIntent = getPendingIntent();
        if (contentIntent != null)
            builder.setContentIntent(contentIntent);
        //
        Resources res = context.getResources();
        String uri = (ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getPackageName() + "/" + R.raw.eat);
        Bitmap img = BitmapFactory.decodeResource
                (res, R.drawable.ic_stat_location_place);
        // todo: maybe show map on the notification image?
        builder
                .setSmallIcon(R.drawable.pin2)
                .setSound(Uri.parse(uri))
                .setLargeIcon(img)
                .setAutoCancel(true)
                .setContentTitle(getTitle())
                .setWhen(System.currentTimeMillis())
                .setContentText(getMessage())
                .addAction(R.drawable.getuser, "Who is it?", getUserIntent())
                .addAction(R.drawable.getmeeting, "See all", getPendingIntent()).build();

        return builder;
    }
}
