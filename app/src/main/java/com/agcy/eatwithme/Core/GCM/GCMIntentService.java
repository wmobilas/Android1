package com.agcy.eatwithme.Core.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.agcy.eatwithme.Core.GCM.Push.Push;
import com.agcy.eatwithme.Core.GCM.Push.PushAccept;
import com.agcy.eatwithme.Core.GCM.Push.PushConfirm;
import com.agcy.eatwithme.Core.GCM.Push.PushMeeting;
import com.agcy.eatwithme.Core.GCM.Push.PushTest;
import com.agcy.eatwithme.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

/**
 * Created by kiolt_000 on 20/08/2014.
 */
public class GCMIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GCMIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {

            Log.i(TAG, "Received: " + extras.toString());
            // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String pushKey = extras.getString("collapse_key");
                String pushJson = extras.getString("json");
                if (pushKey == null) {
                    return;
                }
                Push push = null;
                if (pushKey.equals("meeting")) {
                    push = new Gson().fromJson(pushJson, PushMeeting.class);
                } else {
                    if (pushKey.equals("accept")) {
                        push = new Gson().fromJson(pushJson, PushAccept.class);
                    } else {
                        if (pushKey.equals("confirm")) {
                            push = new Gson().fromJson(pushJson, PushConfirm.class);
                        }
                    }
                }

                if (push == null) {
                    push = new PushTest(pushKey, pushJson);
                }

                push.setContext(getBaseContext());
                push.showNotification();
            }
        }
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Context ctx = this;
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, DemoActivity.class), 0);

        Resources res = ctx.getResources();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.pinw)
                        .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.doge))
                        .setWhen(System.currentTimeMillis())
                        .setAutoCancel(true)
                        .setContentTitle("Test")
                        .setContentText(msg);

        // mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}