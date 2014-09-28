package build.agcy.test1.Core.GCM.Push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import build.agcy.test1.R;

/**
 * Created by kiolt_000 on 09/09/2014.
 */
public abstract class Push {
    protected Context context;
    public final int TEST_NOTIFICATION = -1;

    public final int MEETING_NOTIFICATION = 1;
    public final int ACCEPT_NOTIFICATION = 2;
    public final int CONFIRM_NOTIFICATION = 3;

    public abstract String getTitle();

    public abstract String getMessage();

    public void setContext(Context context) {
        this.context = context;
    }

    protected abstract int getNotificationId();

    public void showNotification() {

        final int NOTIFICATION_ID = getNotificationId();
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
//        (buildr().build()).defaults |= Notification.DEFAULT_VIBRATE;
//        (buildr().build()).sound = Uri.parse("android.resource://" + R.raw.eat);
//        String uri = (ContentResolver.SCHEME_ANDROID_RESOURCE
//                + "://" + context.getPackageName() + "/" + R.raw.eat);
//        (buildr().build()).defaults |= Notification.DEFAULT_SOUND;
//        (buildr().build()).sound = Uri.parse(uri);
//        buildr().setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        mNotificationManager.notify(NOTIFICATION_ID, buildr().build());
    }

    protected NotificationCompat.Builder buildr() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);
        PendingIntent contentIntent = getPendingIntent();
        Intent deleteIntent = new Intent(context, DeleteNotificationReciever.class);
        deleteIntent.putExtra("id", String.valueOf(getNotificationId()));
        Resources res = context.getResources();
        Bitmap img = BitmapFactory.decodeResource
                (res, R.drawable.ic_stat_location_place);
        // todo: maybe show map on the notification image?
        String uri = (ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.getPackageName() + "/" + R.raw.eat);
        builder
                .setSmallIcon(R.drawable.pin2)
                .setLargeIcon(img)
                .setAutoCancel(true)
                .setContentTitle(getTitle())
                .setContentText(getMessage())
                .setWhen(System.currentTimeMillis())
                .setSound(Uri.parse(uri))
                .addAction(R.drawable.cncl, "Dismiss", PendingIntent.getBroadcast(context, 12345, deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .addAction(R.drawable.acpt, "Accept", contentIntent).build();

//        if (contentIntent != null)
//            builder.setContentIntent(contentIntent);
        return builder;
    }

    protected abstract PendingIntent getPendingIntent();
}
