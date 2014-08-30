

package build.agcy.test1.Main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.SocketException;
import java.util.Calendar;

import build.agcy.test1.Api.Errors.ApiError;
import build.agcy.test1.Api.Meetings.MeetingCreateTask;
import build.agcy.test1.Api.Meetings.MeetingGetTask;
import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Fragments.MeetingListFragment;
import build.agcy.test1.Fragments.TimePickerFragment;
import build.agcy.test1.Meetings.MeetingActivity;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;
import build.agcy.test1.Users.UserListActivity;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */

    public static final int GET_FROM_GALLERY = 3;
    ViewPager mViewPager;
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getApplicationContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit().remove("user_meeting_id").commit();
        getApplicationContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).edit().remove("user_meeting_time").commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = getLayoutInflater().inflate(R.layout.activity_main1, null);


        if (EatWithMeApp.isProfileUpdated()){
            (rootView.findViewById(R.id.fragment_profile)).setVisibility(View.GONE);}
        if (!EatWithMeApp.isProfileUpdated()){
            final Button updateButton = (Button) rootView.findViewById(R.id.update_button);
            updateButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    (rootView.findViewById(R.id.fragment_profile)).setVisibility(View.GONE);
                    EatWithMeApp.profileUpdate();
                }});
        }


        setContentView(rootView);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(true);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        String[] s=new String[3];
        s[0]="1";
        s[1]="2";
        s[2]="3";
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
//                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
                            .setText(s[i]));

        }

    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.

        if( EatWithMeApp.isProfileUpdated()){
        mViewPager.setCurrentItem(tab.getPosition());}
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment;
            Bundle args;
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new CreateMeetingFragment();
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.

                    return new MeetingListFragment();
                //todo починить ошибку при перелистывании и удалении карты если она не вторая а первая в табвью
                case 2:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new FirstFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    fragment = new DummySectionFragment();
                    args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    args.putString("rootview",fragment.getActivity().getLayoutInflater().toString());
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Section " + (position + 1);
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
//    public static class LaunchpadSectionFragment extends Fragment {
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // Demonstration of a collection-browsing activity.
//            rootView.findViewById(R.id.demo_collection_button)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Intent intent = new Intent(getActivity(), MapActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//            // Demonstration of navigating to external activities.
//            rootView.findViewById(R.id.demo_external_activity)
//                    .setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Create an intent that asks the user to pick a photo, but using
//                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
//                            // the application from the device home screen does not return
//                            // to the external activity.
//                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
//                            externalActivityIntent.setType("image/*");
//                            externalActivityIntent.addFlags(
//                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                            startActivity(externalActivityIntent);
//                        }
//                    });

//            return rootView;
//        }
//    }

    public void getImage(View v) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }


    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */

    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.explore_new_world, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
    public static class FirstFragment extends Fragment {

        public FirstFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

             View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

            return rootView;
        }
    }
    public static class CreateMeetingFragment extends Fragment {

        public CreateMeetingFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 final Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_create_meeting, container,false);
            final SharedPreferences prefs =  getActivity().getSharedPreferences("auth_prefs", Activity.MODE_PRIVATE);
            final Button createMeetingButton = (Button) rootView.findViewById(R.id.create_meeting);
            final Button  timePickButton = (Button) rootView.findViewById(R.id.time_picker);
            final Button  cancelButton = (Button) rootView.findViewById(R.id.cancel_meeting);
            final TextView waitingAnswer = (TextView) rootView.findViewById(R.id.waiting);
            final int gon=View.GONE;
            final int vis=View.VISIBLE;

            if (prefs.contains("user_meeting_id")){
                createMeetingButton.setVisibility(gon);
                timePickButton.setVisibility(gon);
                waitingAnswer.setVisibility(vis);
                cancelButton.setVisibility(vis);
            }
            cancelButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createMeetingButton.setVisibility(vis);
                    timePickButton.setVisibility(vis);
                    waitingAnswer.setVisibility(gon);
                    cancelButton.setVisibility(gon);
                    getActivity().getSharedPreferences("auth_prefs", Activity.MODE_PRIVATE).edit().remove("user_meeting_id").commit();
                    //todo сделать удаление на сервере
                }
            });
            createMeetingButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!prefs.contains("user_meeting_id")){
                        createMeetingButton.setVisibility(gon);
                        timePickButton.setVisibility(gon);
                        waitingAnswer.setVisibility(vis);
                        cancelButton.setVisibility(vis);
                    Spinner mySpinner =(Spinner) rootView.findViewById(R.id.MeetingType);
                    String latitude=prefs.getString("user_lat", "33.333333");
                    String longitude=prefs.getString("user_lng", "33.333333");
                    String selectedType="Date";
                    try {selectedType = mySpinner.getSelectedItem().toString();}
                    catch (Exception e){
                        Toast.makeText(getActivity().getApplicationContext(), "no spinner item selected", Toast.LENGTH_SHORT).show();
                    }
                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setTitle("Creating meeting...");
                    dialog.setMessage("Please wait");
                    dialog.show();
                        String hour, minute,time;
                        if (!prefs.contains("user_meeting_time")){
                            Calendar c = Calendar.getInstance();
                            time=String.valueOf((c.get(Calendar.HOUR_OF_DAY)))+""+String.valueOf(c.get(Calendar.MINUTE));
                        }
                        else{
                            time = prefs.getString("user_meeting_time", "00").substring(0, 2)+""+prefs.getString("user_meeting_time", "00").substring(2);
                        }
                        if (time.equals("00")){time="0000";}
                        if (time.length()<4){time="0"+time;}
                    MeetingCreateTask task = new MeetingCreateTask(latitude, longitude, time,selectedType) {
                        @Override
                        public void onSuccess(final String i) {
                            MeetingGetTask task = new MeetingGetTask(i) {
                                @Override
                                public void onSuccess(Meeting response) {
                                    dialog.dismiss();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("meeting_id", response.meeting_id);
                                    bundle.putString("meeting_creator", EatWithMeApp.currentUser.username);
                                    bundle.putString("meeting_description", response.description);
                                    bundle.putString("meeting_latitude", response.latitude);
                                    bundle.putString("meeting_longitude", response.longitude);
                                    Intent intent = new Intent(getActivity(), MeetingActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                                @Override
                                public void onError(Exception exp) {
                                    Toast.makeText(getActivity().getApplicationContext(),exp.toString(),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity().getBaseContext(), MeetingActivity.class);
                                    intent.putExtra("meeting_id", i);
                                    SharedPreferences prefs = getActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
                                    prefs.edit().putString("user_meeting_id", i).commit();
                                    Toast.makeText(getActivity().getApplicationContext(),"Meeting id:"+i,Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    }
                            };
                            task.start();
                            //current user id in prefs userid_preferences_key
                        }
                        @Override
                        public void onError(Exception exp) {
                            dialog.dismiss();
                            if (exp instanceof ApiError) {
                                //todo:коды
//                        int code = ((ApiError) exp).getCode();
//                        if (code == ApiError.BADCREDITS) {
//                                Toast.makeText(getActivity().getApplicationContext(), "Check your login and password", Toast.LENGTH_SHORT).show();
//                        }
                            } else {
                                if (exp instanceof SocketException) {
//                                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                                } else {
//                                    Toast.makeText(getActivity().getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    };


//            final ProgressBar mActivityIndicator = (ProgressBar) rootView.findViewById(R.id.login_progress);
//            mActivityIndicator.setVisibility(View.VISIBLE);
                    task.start();
                }}});

            return rootView;
        }
    }
    public static class ThirdFragment extends Fragment {

        public ThirdFragment(){}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_user, container, false);

            return rootView;
        }
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
        startActivity(intent);
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
    public void createMeeting(View v) {

//        }
    }
    public void timePick(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }
}
