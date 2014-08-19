package build.agcy.test1.Start;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import build.agcy.test1.Api.Errors.ApiError;
import build.agcy.test1.BuildConfig;
import build.agcy.test1.Core.Helpers.JsonHelper;
//import build.agcy.test1.GCM.GCMRegistrarCompat;
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
    String SENDER_ID = "AIzaSyAo8u9dVTxGrgxMqM9PMfz_RnUQNptGgLc";// норм? а какой должен быть
    // Денис
    // давай мой
    // AIzaSyAo8u9dVTxGrgxMqM9PMfz_RnUQNptGgLc
    // вот тебе ключ апи эм
    ViewPager mViewPager;
    //String ids=new String();
    Map<String, Object> map = new HashMap<String,Object>();
    String TAG="StartActivity";
    TextView mDisplay;
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    SharedPreferences prefs;
    Context context;
    String regid="";
    private void saveData(String response) {
        try {
            JSONObject json= (JSONObject) new JSONTokener(response).nextValue();
//            JSONArray json2 = json.getJSONArray("response");
            map = JsonHelper.toMap(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
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

            //if (regid.isEmpty())
            {
                registerInBackground();
            }
        } else {
            Log.e(TAG, "No valid Google Play Services APK found.");
        }
        // todo: если токен загружен, то это окно нужно сразу же закрыть и открыть главное меню. пока можно оставить
//        {
//            что тут происходит ? чВ должно fragments_tabbd прикрутить
//            ApiTaskBase task = new ApiTaskBase("", new ArrayList<NameValuePair>()// это аргументы, раз их нет, то не надо их писать.
//                    , false, false) {
//                @Override
//                protected void onPostExecute(Object response) {
////                Toast.makeText(getBaseContext(), (CharSequence) response, Toast.LENGTH_SHORT).show();
//                    saveData(response.toString());
////                TextView myAwesomeTextView = (TextView)findViewById(R.id.usrnm);
//                    //List<Object> listid2 = new ArrayList<Object>();
//                    ArrayList arrlist = (ArrayList) activity_map.get("response");
//                    Map innermap;
//                    int j = arrlist.size(); //чтобы не уменьшался лист в 2 раза
//                    for (int i = 0; i < j; i++) {
//                        //Object object=list.remove(list.size()-1) + " , ";
//                        innermap = (HashMap) arrlist.get(i);
//                        //innermap=(HashMap)innermap.get(0);
//                        Map.Entry entry = (Map.Entry) innermap.entrySet().iterator().next();
//                        ids += entry.getValue() + " , ";
//
////                myAwesomeTextView.append(listid.remove(listid.size()-1));
////                myAwesomeTextView.append(" , ");
//                    }
//                    ids = ids.substring(0, ids.length() - 3);
//                }
//            };
//
//
//            task.execute();
//        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.}
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        // я сейчас немного тебе покажу и пойду спать, мне пиздец херово.
        // Ночью проснусь, хочу видеть твой комит, доделаю так, чтобы Ване было что показывать.
        // А ты будешь рефакторить потом всё, ок?ок
        // потому что много кода писал ты, много я, я хочу, чтобы ты понимал как пишется
        // правильно. ты ведь видишь как я структурно делаю?угу
        // вот и ты должен так уметь. поэтому разберёшь весь код, что я писал в доль и поперёк
        // и тоже смотри todo ок?
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(StartActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(position==1){
                return new MeetingFragment();
            }
            if(position==2){
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

    public static class LoginFragment extends Fragment{
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }
    public static class MeetingFragment extends Fragment{
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_explore, container, false);
            return rootView;
        }
    }
//    protected String wifiIpAddress(Context context) {
//        WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
//        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
//
//        // Convert little-endian to big-endianif needed
//        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
//            ipAddress = Integer.reverseBytes(ipAddress);
//        }
//
//        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
//
//        String ipAddressString;
//        try {
//            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
//        } catch (UnknownHostException ex) {
//            Log.e("WIFIIP", "Unable to get host address.");
//            ipAddressString = null;
//        }
//
//        return ipAddressString;
//    }

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
        Intent intent= new Intent(getBaseContext(), MeetingActivity.class);
        intent.putExtras(bundle);
     //   startActivity(intent);
        //startActivity(new Intent(getBaseContext(), UserListActivity.class));
        startActivity(new Intent(getBaseContext(), MeetingsListActivity.class));
    }
public void registerProfile(View v){
    final View rootView = getLayoutInflater().inflate(R.layout.fragment_explore, null);
    final ProgressBar mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.login_progress);

}
    public void getImage(View v){
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
        }
    public void login(View v){

        if(mViewPager.getCurrentItem()<2){
            mViewPager.setCurrentItem(2, true);
        }else {
            LoginTask task = new LoginTask("wmobilas", "123qweASD") {
                @Override
                public void onSuccess(CurrentUser response) {
                    final View rootView = getLayoutInflater().inflate(R.layout.fragment_login, null);
                    final ProgressBar mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.login_progress);
                    mActivityIndicator.setVisibility(View.GONE);

                    // todo: мы залогинились, переходим дальше, и сохраняем данные, которые дали ок
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
                    //todo: надо сохранить этот токен в SharedPreferences чтобы не логиниться каждый раз,
                    // а при запущенном приложении держать его в EatWithMeApp.token, чтобы легко можно
                    // получать к нему доступ.
                    getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE).edit().putString("application_token", token).commit();
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
        final SharedPreferences prefs = getGCMPreferences(context);
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
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                final View rootView = getLayoutInflater().inflate(R.layout.fragment_login, null);
                final TextView mDisplay = (TextView) rootView.findViewById(R.id.response);
                // што это ну кудато вывод хотело иначе эррор на mdisplay
                mDisplay.append(msg);
            }
        }.execute(null, null, null);
    }
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        Log.i(TAG, "sendRegistrationIdToBackend");

    }
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
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
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
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
