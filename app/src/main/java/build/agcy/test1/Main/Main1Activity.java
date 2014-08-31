

package build.agcy.test1.Main;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
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
import android.widget.TextView;

import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Meetings.MeetingListFragment;
import build.agcy.test1.Fragments.TimePickerFragment;
import build.agcy.test1.Meetings.MeetingActivity;
import build.agcy.test1.R;
import build.agcy.test1.Users.UserListActivity;

public class Main1Activity extends FragmentActivity implements ActionBar.TabListener {

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main1);



        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
        String[] s = new String[3];
        s[0] = "1";
        s[1] = "2";
        s[2] = "3";
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
        if (EatWithMeApp.isProfileUpdated()) {
            mViewPager.setCurrentItem(tab.getPosition());
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

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
                    //return new CreateMeetingFragment();
                case 1:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.

                    //return new MeetingListFragment();
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
                    args.putString("rootview", fragment.getActivity().getLayoutInflater().toString());
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

    public void getImage(View v) {
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }


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

        public FirstFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_explore, container, false);

            return rootView;
        }
    }

    public static class ThirdFragment extends Fragment {

        public ThirdFragment() {
        }

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
