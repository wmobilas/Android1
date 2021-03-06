package com.agcy.eatwithme.Start;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agcy.eatwithme.Api.Errors.ApiError;
import com.agcy.eatwithme.Api.PushRegisterTask;
import com.agcy.eatwithme.Core.GCM.GCMRegistrationTask;
import com.agcy.eatwithme.Core.Helpers.FindMe;
import com.agcy.eatwithme.EatWithMeApp;
import com.agcy.eatwithme.Main.MainActivity;
import com.agcy.eatwithme.Models.CurrentUser;
import com.agcy.eatwithme.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.net.SocketException;
import java.util.List;
import java.util.Locale;


public class StartActivity extends FragmentActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "application_version_code";
    String SENDER_ID = "181401788596";
    ViewPager mViewPager;
    String TAG = "agcy.test";
    GoogleCloudMessaging gcm;
    Context context;
    String regid = "";
    String username = "";
    String password = "";
    TextView username_login;
    TextView password_login;
    MediaPlayer mp;
    private static long back_pressed;

    @Override
    protected void onPause() {
        if (this.isFinishing()) {
            mp.stop();
        }
        context = this;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        if (!taskInfos.isEmpty()) {
            ComponentName topActivity = taskInfos.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                mp.stop();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (this.isFinishing()) {
            if (mp != null) {
                mp.stop();
            }
        }
        context = this;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = activityManager.getRunningTasks(1);
        if (!taskInfos.isEmpty()) {
            ComponentName topActivity = taskInfos.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                if (mp != null) {
                    mp.stop();
                }
            }
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        context = getApplicationContext();


        FindMe.please(context, new FindMe.FindMeListener() {
            @Override
            public void foundLocation(String provider, Location location) {
                Log.d(TAG, " " + location.getLatitude() + " " + location.getLongitude());
            }

            @Override
            public void couldntFindLocation() {
            }
        });

        if (EatWithMeApp.token != null) {
            startActivity(new Intent(this, MainActivity.class));
            registerGCM();
            finish();
            return;
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mp = MediaPlayer.create(StartActivity.this, R.raw.music);
        mp.setLooping(true);
        mp.setVolume(0.5f, 0.5f);
        AudioManager manager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        if (!manager.isMusicActive()) {
            mp.start();
        }
    }

    private void registerGCM() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            registerInBackground(!regid.isEmpty());
//            registerInBackground();
        } else {
            Log.e(TAG, "No valid Google Play Services APK found.");
            Toast.makeText(context, "NO PLAY SERVICES", Toast.LENGTH_LONG).show();
        }
    }

    private SharedPreferences getGCMPreferences() {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            if (position == 1) {
//                return new SecondPageFragment();
//            }
//            if (position == 2) {
//                return new ThirdPageFragment();
//            }
            //todo: send data to fragment
            return WelcomeFragment.newInstance(R.drawable.doge, R.string.hello_world);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.app_name).toUpperCase(l);
                case 1:
                    return getString(R.string.app_name).toUpperCase(l);
                case 2:
                    return getString(R.string.app_name).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class WelcomeFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_IMG_RES = "img_res";
        private static final String ARG_TXT_RES = "txt_res";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static WelcomeFragment newInstance(int imageResourceId, int textResourceId) {
            WelcomeFragment fragment = new WelcomeFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_IMG_RES, imageResourceId);
            args.putInt(ARG_TXT_RES, textResourceId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.activity_welcome, container, false);
            final Button joinButton = (Button) rootView.findViewById(R.id.join_button);
            final Button alreadyButton = (Button) rootView.findViewById(R.id.already_button);
            final EditText password = (EditText) rootView.findViewById(R.id.password_login);
            final EditText username = (EditText) rootView.findViewById(R.id.username_login);
            ImageView myImageView = (ImageView) rootView.findViewById(R.id.lindrawable);
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade);
            myImageView.startAnimation(myFadeInAnimation);
            username.setTypeface(Typeface.SANS_SERIF);
            username.setTransformationMethod(new SingleLineTransformationMethod());
            password.setTypeface(Typeface.SANS_SERIF);
            password.setTransformationMethod(new PasswordTransformationMethod());
            //  GCMRegistrarCompat.checkDevice(this);
            //if (BuildConfig.DEBUG) {
            //     GCMRegistrarCompat.checkManifest(this);}

            final Button login = (Button) (rootView.findViewById(R.id.login_button));
            final Button register = (Button) (rootView.findViewById(R.id.register_button));
            joinButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            register.setVisibility(View.VISIBLE);
                            login.setVisibility(View.GONE);
                            register.setAlpha(Float.parseFloat("0.9"));
                            joinButton.setVisibility(View.GONE);
                            alreadyButton.setVisibility(View.VISIBLE);
                        }
                    });
            alreadyButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            login.setVisibility(View.VISIBLE);
                            register.setVisibility(View.GONE);
                            login.setAlpha(Float.parseFloat("0.9"));
                            alreadyButton.setVisibility(View.GONE);
                            joinButton.setVisibility(View.VISIBLE);
                        }
                    });
//            Bundle args = getArguments();
            //todo: bind args
            TextView description = (TextView) rootView.findViewById(R.id.textWelcome);
            description.setTypeface(Typeface.createFromAsset(getActivity().getBaseContext().getAssets(), "fonts/fredok.ttf"));
//            MainActivity.applyTypeface(MainActivity.getParentView(rootView), MainActivity.getTypeface(getActivity().getApplicationContext()));
            return rootView;
        }
    }

//    public static class SecondPageFragment extends Fragment {
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            return inflater.inflate(R.layout.fragment_login, container, false);
//        }
//    }
//
//    public static class ThirdPageFragment extends Fragment {
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//
//            return inflater.inflate(R.layout.fragment_login, container, false);
//        }
//    }

    public void registerProfile(View v) {
        username_login = (TextView) findViewById(R.id.username_login);
        username = username_login.getText().toString();
        password_login = (TextView) findViewById(R.id.password_login);
        password = password_login.getText().toString();

        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Please fill form", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Registering...");
        dialog.setMessage("Please wait");
        dialog.show();

        RegisterTask task = new RegisterTask(username, password) {
            @Override
            public void onSuccess(CurrentUser currentUser) {
                dialog.dismiss();
                EatWithMeApp.saveCurrentUser(currentUser);
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                registerGCM();
                finish();
            }

            @Override
            public void onError(Exception exp) {
                dialog.dismiss();
                Button login = (Button) (findViewById(R.id.login_button));
                Button register = (Button) (findViewById(R.id.register_button));
                login.setVisibility(View.VISIBLE);
                register.setVisibility(View.GONE);
                login.setAlpha(Float.parseFloat("0.9"));
                (findViewById(R.id.already_button)).setVisibility(View.GONE);
                (findViewById(R.id.join_button)).setVisibility(View.VISIBLE);
                if (exp instanceof ApiError) {
                    //todo:коды
                    int code = ((ApiError) exp).getCode();
//                        if (code == ApiError.BADCREDITS) {
                    String message;
                    switch (code) {
                        case 500:
                            message = "Internal error";
                            break;
                        case 404:
                            message = "Something went wrong";
                            break;
                        case 403:
                            message = "Authorisation error";
                            break;
                        default:
                            message = "Unknown error";
                            break;
                    }
                    Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();

                } else {
                    if (exp instanceof SocketException) {
                        Toast.makeText(StartActivity.this, "Check your internet connection" + exp.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StartActivity.this, "Unexpected error" + exp.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onTokenRecieved(String token) {
                EatWithMeApp.saveToken(token);
            }
        };

        task.start();
    }


    public void login(View v) {

        username_login = (TextView) findViewById(R.id.username_login);
        username = username_login.getText().toString();
        password_login = (TextView) findViewById(R.id.password_login);
        password = password_login.getText().toString();
        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, "Please fill form", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog[] dialog = {new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)};
        dialog[0].setCancelable(false);
        dialog[0].setCanceledOnTouchOutside(false);
        dialog[0].setTitle("Logging in");
        dialog[0].setMessage("Please wait");
        dialog[0].show();

        LoginTask task = new LoginTask(username, password) {
            @Override
            public void onSuccess(CurrentUser currentUser) {
                try {
                    if ((dialog[0] != null) && dialog[0].isShowing()) {
                        dialog[0].dismiss();
                        EatWithMeApp.saveCurrentUser(currentUser);
                        startActivity(new Intent(getBaseContext(), MainActivity.class));
                        registerGCM();
                        finish();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                    dialog[0] = null;
                }
            }

            @Override
            public void onError(Exception exp) {
                try {
                    if ((dialog[0] != null) && dialog[0].isShowing()) {
                        dialog[0].dismiss();
                        if (exp instanceof ApiError) {
                            //todo:коды
//                        int code = ((ApiError) exp).getCode();
//                        if (code == ApiError.BADCREDITS) {
                            int code = ((ApiError) exp).getCode();
//                        if (code == ApiError.BADCREDITS) {
                            String message;
                            switch (code) {
                                case 500:
                                    message = "Internal error";
                                    break;
                                case 404:
                                    message = "Something went wrong";
                                    break;
                                case 403:
                                    message = "Authorisation error";
                                    break;
                                default:
                                    message = "Check your login and password";
                                    break;
                            }
                            Toast.makeText(StartActivity.this, message, Toast.LENGTH_SHORT).show();

//                            Toast.makeText(StartActivity.this, "Check your login and password", Toast.LENGTH_SHORT).show();
//                        }

                        } else {
                            if (exp instanceof SocketException) {
                                Toast.makeText(StartActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(StartActivity.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                    dialog[0] = null;
                }
            }

            @Override
            public void onTokenRecieved(String token) {
                EatWithMeApp.saveToken(token);
            }
        };
        task.start();
    }


    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        Log.i(TAG, "Result code: " + resultCode + ". With error message: " + GooglePlayServicesUtil.getErrorString(resultCode));

        if (resultCode != ConnectionResult.SUCCESS) {


            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.w(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }


    private void registerInBackground(boolean unregister) {
        GCMRegistrationTask task = new GCMRegistrationTask(this, unregister) {
            @Override
            public void onSuccess(String regid) {
                storeRegistrationId(regid);
                sendRegistrationIdToBackend(regid);
            }

            @Override
            public void onError(Exception exp) {
                Toast.makeText(getBaseContext(), "Cant get register id: " + exp.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private void sendRegistrationIdToBackend(String regid) {
        PushRegisterTask pushRegisterTask = new PushRegisterTask(regid) {
            @Override
            public void onSuccess(Boolean response) {

            }

            @Override
            public void onError(Exception exp) {

            }
        };
        pushRegisterTask.start();
        Log.i(TAG, "sendRegistrationIdToBackend");
    }

    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = getGCMPreferences();
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }


}