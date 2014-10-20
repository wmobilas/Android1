package com.agcy.eatwithme.Core.GCM;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
    public GCMBroadcastReceiver() {
    }

    private String LOG_TAG = "GCMBRoadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
//        String regId = intent.getExtras().getString("registration_id");
//        if(regId != null && !regId.equals("")) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
        Log.i(LOG_TAG, "Recieved");
//        }

    }
}
