package com.agcy.eatwithme.Core.GCM.Push;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Freeman on 18.09.2014.
 */
public class DeleteNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("shuffTest", "I Arrived!!!!");
        //Toast.makeText(context, "Alarm worked!!", Toast.LENGTH_LONG).show();

        Bundle answerBundle = intent.getExtras();
        int userAnswer = answerBundle.getInt("id");
//        if (userAnswer == 1) {
//            Log.v("shuffTest", "Pressed YES");
//        } else if (userAnswer == 2) {
//            Log.v("shuffTest", "Pressed NO");
//        } else if (userAnswer == 3) {
//            Log.v("shuffTest", "Pressed MAYBE");
//        }

        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.cancel(userAnswer);
    }
}
