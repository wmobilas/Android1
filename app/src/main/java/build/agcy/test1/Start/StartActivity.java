package build.agcy.test1.Start;

import java.net.SocketException;
import java.util.HashMap;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import java.util.Locale;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import build.agcy.test1.Api.Errors.ApiError;
import build.agcy.test1.Core.Helpers.JsonHelper;
import build.agcy.test1.Fragments.TimePickerFragment;
import build.agcy.test1.Meetings.MeetingsListActivity;
import build.agcy.test1.Models.CurrentUser;
import build.agcy.test1.R;
import build.agcy.test1.Users.UserActivity;


public class StartActivity extends FragmentActivity {
   // public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

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
    ViewPager mViewPager;
    //List<Object> list = new ArrayList<Object>();
    String ids=new String();
    Map<String, Object> map = new HashMap();
    private void saveData(String response) {
        try {
            JSONObject json= (JSONObject) new JSONTokener(response).nextValue();
            JSONArray json2 = json.getJSONArray("response");
            //for(int i=0;i<json2.length();i++){
            //list.add(json2.getJSONObject(i));}
            //.get("id").toString());}
            map = JsonHelper.toMap(json);
            //list=JsonHelper.toList(json2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        /*
        что тут происходит? чВ должно fragments_tabbd прикрутить
        ApiTaskBase task = new ApiTaskBase("", new ArrayList<NameValuePair>()// это аргументы, раз их нет, то не надо их писать.
                , false, false) {
            @Override
            protected void onPostExecute(Object response) {
//                Toast.makeText(getBaseContext(), (CharSequence) response, Toast.LENGTH_SHORT).show();
                saveData(response.toString());
//                TextView myAwesomeTextView = (TextView)findViewById(R.id.usrnm);
                //List<Object> listid2 = new ArrayList<Object>();
                ArrayList arrlist =(ArrayList) activity_map.get("response");
                Map innermap;
                int j=arrlist.size(); //чтобы не уменьшался лист в 2 раза
                for (int i=0; i<j; i++) {
                    //Object object=list.remove(list.size()-1) + " , ";
                    innermap=(HashMap)arrlist.get(i);
                    //innermap=(HashMap)innermap.get(0);
                    Map.Entry entry = (Map.Entry) innermap.entrySet().iterator().next();
                    ids+=entry.getValue()+" , ";

//                myAwesomeTextView.append(listid.remove(listid.size()-1));
//                myAwesomeTextView.append(" , ");
            }
            ids=ids.substring(0,ids.length()-3);
            }
        };


        task.execute();
        //он запускает новый поток, который заходит внутрь doInBackground()
        // это ведь не пост запрос, а гет, поэтому нужен фолс. тут видишь переключатель типа запроса?
        // после завершенияdoInBacgrkound() он возвращается в тот поток,
        // в котором вызывалось, и делает onPostExecute() теперь надо чтобы ты потренировался со всякими вьюшками, фрагментами, списками, адаптерами. лол. ты ведь что-то знаешь?смотря что
        // то есть сейчас этот постЭкзекьют - полноценный колбьек, о котором тогода у нас шла речьясн
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.*/
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        LoginTask task = new LoginTask("wmobilas","123qweASD") {
            @Override
            public void onSuccess(CurrentUser response) {
                // todo: remove progressbar
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
        };
        // todo: show progress bar
        task.start();
        // точно так же делаются запросы с юзерами и тд. тоже пример покажу сейчас, чтобы было наглядно.
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
            return PlaceholderFragment.newInstance(position + 1);
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
                    return getString(R.string.text_here).toUpperCase(l);
                case 1:
                    return getString(R.string.text_here).toUpperCase(l);
                case 2:
                    return getString(R.string.text_here).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
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

    public void peopleOpen(View view) {Intent intent = new Intent(this, MeetingsListActivity.class);
        EditText usrnm = (EditText) findViewById(R.id.usrnm);
        EditText pass = (EditText) findViewById(R.id.pass);
        String[] message = new String[3];
        message[0]=usrnm.getText().toString();
        message[1]=pass.getText().toString();
        message[2]=ids;

        LoginTask loginTask = new LoginTask("superuser","superuser") {
            @Override
            public void onSuccess(CurrentUser response) {
                // todo: save curre

            }

            @Override
            public void onError(Exception exp) {

            }
        };

        Bundle bundle = new Bundle();
        bundle.putStringArray("1",message);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void showTimePickerDialog(View v) {
        /*
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
        */

        Bundle bundle = new Bundle();
        bundle.putString("userid","c68fe120-f4f4-41ff-b885-9cb97884704f");// вот такие айдишники будут у юзеров
        Intent intent= new Intent(getBaseContext(), UserActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
