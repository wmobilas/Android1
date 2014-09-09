package build.agcy.test1.Core.GCM.Push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context);
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = getPendingIntent();

        Resources res = context.getResources();
        Bitmap img = BitmapFactory.decodeResource
                (res, R.drawable.doge);
        // todo: maybe show map on the notification image?
        builder
                .setSmallIcon(R.drawable.pin)
                .setLargeIcon(img)
                .setAutoCancel(true)
                .setContentTitle(getTitle())
                .setContentText(getMessage());
        // todo: implement buttons
        if (contentIntent != null)
            builder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    protected abstract PendingIntent getPendingIntent();
}
