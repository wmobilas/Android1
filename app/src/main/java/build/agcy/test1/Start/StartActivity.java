package build.agcy.test1.Start;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import build.agcy.test1.Api.Errors.ApiError;
//import build.agcy.test1.GCM.GCMRegistrarCompat;
import build.agcy.test1.Core.GCM.GCMRegistrationTask;
import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Main.MainActivity;
import build.agcy.test1.Meetings.MeetingActivity;
import build.agcy.test1.Meetings.MeetingsListActivity;
import build.agcy.test1.Models.CurrentUser;
import build.agcy.test1.R;
import build.agcy.test1.Users.UserListActivity;


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

    public static final int GET_FROM_GALLERY = 3;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "11";
    public static final String PROPERTY_REG_ID = "11";
    private static final String PROPERTY_APP_VERSION = "application_version_code";

    ViewPager mViewPager;
    Map<String, Object> map = new HashMap<String, Object>();
    String TAG = "StartActivity";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regid = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //  GCMRegistrarCompat.checkDevice(this);
        //if (BuildConfig.DEBUG) {
        //     GCMRegistrarCompat.checkManifest(this);}
        context = getApplicationContext();
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.e(TAG, "No valid Google Play Services APK found.");
        }
        if (EatWithMeApp.token != null) {
            //finish();
            //startActivity(new Intent(this, MainActivity.class));
        }
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

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
                return new RegisterFragment();
            }
            if (position == 2) {
                return new LoginFragment();
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
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
            Bundle args = getArguments();
            // todo: bind args
            return rootView;
        }
    }

    public static class LoginFragment extends Fragment {
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }

    public static class RegisterFragment extends Fragment {
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
            return rootView;
        }
    }


    public void showUsers(View view) {
        /*
        EditText usrnm = (EditText) findViewById(R.id.usrnm);
        EditText pass = (EditText) findViewById(R.id.pass);
        String[] message = new String[3];
        message[0]=usrnm.getText().toString();
        message[1]=pass.getText().toString();
        message[2]=ids;
        */


        startActivity(new Intent(this, UserListActivity.class));
    }

    public void showMeetings(View v) {

        Bundle bundle = new Bundle();
        //bundle.putString("userid", "c68fe120-f4f4-41ff-b885-9cb97884704f");// вот такие айдишники будут у юзеров
        bundle.putString("meetingid", "c68fe120-f4f4-41ff-b885-9cb97884704f");// вот такие айдишники будут у юзеров
        //Intent intent= new Intent(getBaseContext(), UserActivity.class);
        Intent intent = new Intent(getBaseContext(), MeetingActivity.class);
        intent.putExtras(bundle);
        //   startActivity(intent);
        //startActivity(new Intent(getBaseContext(), UserListActivity.class));
        startActivity(new Intent(getBaseContext(), MeetingsListActivity.class));
    }

    public void registerProfile(View v) {
        final View rootView = getLayoutInflater().inflate(R.layout.fragment_explore, null);
        final ProgressBar mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.login_progress);

    }

    public void getImage(View v) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    public void login(View v) {

        if (mViewPager.getCurrentItem() < 2) {
            mViewPager.setCurrentItem(2, true);
        } else {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setTitle("Logging in");
            dialog.setMessage("Please wait");
            dialog.show();
            LoginTask task = new LoginTask("wmobilas", "123qweASD") {
                @Override
                public void onSuccess(CurrentUser currentUser) {
                    dialog.dismiss();
                    EatWithMeApp.saveCurrentUser(currentUser);
                    startActivity(new Intent(getBaseContext(), MainActivity.class));
                    finish();
                }

                @Override
                public void onError(Exception exp) {
                    // а нет, норм, туплю
                    if (exp instanceof ApiError) {
                        int code = ((ApiError) exp).getCode();
                        if (code == ApiError.BADCREDITS) {
                            Toast.makeText(getApplicationContext(), "Check your login and password", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (exp instanceof SocketException) {
                            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                        }
                        // удали дурацкий иврит, ты им пользуешься? нередк
                        // ты понял как вообще должно происходить?что именно? ну запрос
                    }
                }

                @Override
                public void onTokenRecieved(String token) {
                    EatWithMeApp.saveToken(token);
                }
            };

            final View rootView = getLayoutInflater().inflate(R.layout.fragment_login, null);
            final ProgressBar mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.login_progress);
            mActivityIndicator.setVisibility(View.VISIBLE);
            task.start();
        }
    }

    public void register(View view) {
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
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

    private void registerInBackground() {
        GCMRegistrationTask task = new GCMRegistrationTask(this) {
            @Override
            public void onSuccess(String regid) {
                storeRegistrationId(regid);
                sendRegistrationIdToBackend(regid);
            }

            @Override
            public void onError(Exception exp) {
                Toast.makeText(getBaseContext(),"Cant get register id: " + exp.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        };
        task.execute();
    }

    private void sendRegistrationIdToBackend(String regid) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ImageButton dogeButton = (ImageButton) findViewById(R.id.dogeButton);
                dogeButton.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}