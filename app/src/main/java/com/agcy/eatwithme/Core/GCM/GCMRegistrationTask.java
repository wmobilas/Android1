package com.agcy.eatwithme.Core.GCM;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.agcy.eatwithme.Api.TaskCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

/**
 * Created by kiolt_000 on 20/08/2014.
 */
public abstract class GCMRegistrationTask extends AsyncTask<Void, Void, Object> implements TaskCallback<String> {

    private static final String LOG_TAG = "GCMRegistrtionTask";
    private final Context context;
    private final boolean unregister;

    String SENDER_ID = "331939190116";

    public GCMRegistrationTask(Context context, boolean unregister) {
        this.context = context;
        this.unregister = unregister;
    }

    @Override
    protected Object doInBackground(Void... params) {
        String regid = "";
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        if (unregister) {
            try {
                gcm.unregister();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            regid = gcm.register("331939190116", "879830610296", "181401788596");//todo service not available
//            regid = gcm.register(SENDER_ID);
            Log.i(LOG_TAG, "Device registered, registration ID=" + regid);
            // You should send the registration ID to your server over HTTP, so it
            // can use GCM/HTTP or CCS to send messages to your app.

        } catch (IOException ex) {
            Log.e(LOG_TAG, "Error :" + ex.getMessage(), ex);
            return ex;
        }
        return regid;
    }

    @Override
    protected void onPostExecute(Object o) {
        if (o instanceof Exception) {
            onError((Exception) o);
        } else {
            onSuccess((String) o);
        }
    }
}
