package build.agcy.test1.Core.GCM;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import build.agcy.test1.Api.TaskCallback;

/**
 * Created by kiolt_000 on 20/08/2014.
 */
public abstract class GCMRegistrationTask extends AsyncTask<Void, Void, Object> implements TaskCallback<String> {

    private static final String LOG_TAG = "GCMRegistrtionTask";
    private final Context context;

    String SENDER_ID = "331939190116";

    public GCMRegistrationTask(Context context) {
        this.context = context;
    }

    @Override
    protected Object doInBackground(Void... params) {
        String regid = "";
        try {
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);

            regid = gcm.register(SENDER_ID);
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
        if(o instanceof Exception){
            onError((Exception) o);
        }else{
            onSuccess((String) o);
        }
    }
}
