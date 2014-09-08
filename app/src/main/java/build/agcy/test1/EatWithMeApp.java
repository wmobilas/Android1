package build.agcy.test1;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import build.agcy.test1.Models.CurrentUser;

/**
 * Created by kiolt_000 on 19/08/2014.
 */
public class EatWithMeApp extends Application {
    public static String token;
    public static CurrentUser currentUser;
    private static EatWithMeApp app;
    private final static String AUTH_PREFS = "auth_prefs";
    private final static String TOKEN_PREFERENCES_KEY = "token_preferences_key";
    private final static String USERNAME_PREFERENCES_KEY = "username_preferences_key";
    private final static String USERID_PREFERENCES_KEY = "userid_preferences_key";
    private final static String PHOTO_PREFERENCES_KEY = "PHOTO_preferences_key";
    private final static String UPDATED_PROFILE_COUNT = "UPDATED_PROFILE_COUNT".toLowerCase();

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = getSharedPreferences(AUTH_PREFS, Activity.MODE_MULTI_PROCESS);
        if (prefs.contains(TOKEN_PREFERENCES_KEY)) {
            token = prefs.getString(TOKEN_PREFERENCES_KEY, null);
            currentUser = new CurrentUser();
            currentUser.username = prefs.getString(USERNAME_PREFERENCES_KEY, "AnonymousDoge");
            currentUser.id = prefs.getString(USERID_PREFERENCES_KEY, "");
            currentUser.photo = prefs.getString(PHOTO_PREFERENCES_KEY, "http://cs616230.vk.me/v616230567/151af/cP4wx9MZT-w.jpg");
        }
        app = this;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static void saveToken(String token) {
        EatWithMeApp.token = token;
        SharedPreferences prefs = app.getSharedPreferences(AUTH_PREFS, Activity.MODE_MULTI_PROCESS);
        prefs.edit().putString(TOKEN_PREFERENCES_KEY, token).apply();
    }

    public static void saveCurrentUser(CurrentUser currentUser) {
        SharedPreferences prefs = app.getSharedPreferences(AUTH_PREFS, Activity.MODE_MULTI_PROCESS);
        prefs.edit()
                .putString(USERNAME_PREFERENCES_KEY, currentUser.username)
                .putString(USERID_PREFERENCES_KEY, currentUser.id)
                .putString(PHOTO_PREFERENCES_KEY, currentUser.photo)
                .apply();
        EatWithMeApp.currentUser = currentUser;
    }

    public static boolean isOwner(String creator) {
        return currentUser.id.equals(creator);
    }
}
