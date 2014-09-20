package build.agcy.test1.Start;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import build.agcy.test1.R;

/**
 * Created by Freeman on 12.09.2014.
 */
public class Music extends Service {

    MediaPlayer mp;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate() {
        mp = MediaPlayer.create(this, R.raw.coffe);
        mp.setLooping(true);
    }

    public void onDestroy() {
        mp.stop();
    }

    public void onStart(Intent intent, int startid) {

        Log.d("coffee", "On start");
        mp.start();
    }
}