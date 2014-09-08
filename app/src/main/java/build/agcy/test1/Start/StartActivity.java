package build.agcy.test1.Start;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import build.agcy.test1.Api.Errors.ApiError;
import build.agcy.test1.Api.PushRegisterTask;
import build.agcy.test1.Core.GCM.GCMRegistrationTask;
import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Main.MainActivity;
import build.agcy.test1.Models.CurrentUser;
import build.agcy.test1.R;


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

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PROPERTY_REG_ID = "11";
    private static final String PROPERTY_APP_VERSION = "application_version_code";

    ViewPager mViewPager;
    Map<String, Object> map = new HashMap<String, Object>();
    String TAG = "agcy.test";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regid = "";
    String username = "";
    String password = "";
    TextView username_login;
    TextView password_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //  GCMRegistrarCompat.checkDevice(this);
        //if (BuildConfig.DEBUG) {
        //     GCMRegistrarCompat.checkManifest(this);}
        context = getApplicationContext();
        if (EatWithMeApp.token != null) {
            startActivity(new Intent(this, MainActivity.class));
            registerGCM();
            finish();
            return;
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


    }

    private void registerGCM() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            registerInBackground(!regid.isEmpty());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tabbd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 1) {
                return new SecondPageFragment();
            }
            if (position == 2) {
                return new ThirdPageFragment();
            }
            //todo: send data to fragment
            return WelcomeFragment.newInstance(R.drawable.doge, R.string.hello_world);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.doge).toUpperCase(l);
                case 1:
                    return getString(R.string.explore_new_world).toUpperCase(l);
                case 2:
                    return getString(R.string.login).toUpperCase(l);
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
            final View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            final Button JoinButton = (Button) rootView.findViewById(R.id.join_button);
            JoinButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ((Button) rootView.findViewById(R.id.login_button)).setVisibility(View.GONE);
                            ((Button) rootView.findViewById(R.id.join_button)).setVisibility(View.GONE);
                            ((Button) rootView.findViewById(R.id.register_button)).setVisibility(View.VISIBLE);
                            Log.d("build.agcy", "registration");
                        }
                    });
            Bundle args = getArguments();
//            todo: bind args
            return rootView;
        }
    }

    public static class SecondPageFragment extends Fragment {
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_login, container, false);
        }
    }

    public static class ThirdPageFragment extends Fragment {
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_login, container, false);
        }
    }

    public void registerProfile(View v) {
        username_login = (TextView) findViewById(R.id.username_login);
        username = username_login.getText().toString();
        password_login = (TextView) findViewById(R.id.password_login);
        password = password_login.getText().toString();
//        final Button login_button =(Button) findViewById(R.id.login_button);
//        login_button.setVisibility(View.GONE);
//        final Button join_button =(Button) findViewById(R.id.join_button);
//        login_button.setVisibility(View.GONE);
//        final Button register_button =(Button) findViewById(R.id.register_button);
//        login_button.setVisibility(View.VISIBLE);

        if (username.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill form", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
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
                ((Button) findViewById(R.id.login_button)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.join_button)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.register_button)).setVisibility(View.GONE);
                if (exp instanceof ApiError) {
                    //todo:коды
//                        int code = ((ApiError) exp).getCode();
//                        if (code == ApiError.BADCREDITS) {
                    Toast.makeText(getApplicationContext(), "Can`t register now. Please try later" + exp.toString(), Toast.LENGTH_SHORT).show();
//                        }

                } else {
                    if (exp instanceof SocketException) {
                        Toast.makeText(getApplicationContext(), "Check your internet connection" + exp.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unexpected error" + exp.toString(), Toast.LENGTH_SHORT).show();
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
        /*
        username="kioltk";
        password="123qweASD";*/
        if (username.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill form", Toast.LENGTH_SHORT).show();
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("Logging in");
        dialog.setMessage("Please wait");
        dialog.show();

        LoginTask task = new LoginTask(username, password) {
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
                if (exp instanceof ApiError) {
                    //todo:коды
//                        int code = ((ApiError) exp).getCode();
//                        if (code == ApiError.BADCREDITS) {
                    Toast.makeText(getApplicationContext(), "Check your login and password", Toast.LENGTH_SHORT).show();
//                        }

                } else {
                    if (exp instanceof SocketException) {
                        Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
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
}