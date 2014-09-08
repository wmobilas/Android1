package build.agcy.test1.Main;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import build.agcy.test1.Core.Helpers.FindMe;
import build.agcy.test1.Fragments.MapFragment;
import build.agcy.test1.Meetings.CreateMeetingFragment;
import build.agcy.test1.Meetings.MeetingListFragment;
import build.agcy.test1.R;

public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ProfileFragment();
                break;
            case 1:
                fragment = new CreateMeetingFragment();
                break;
            case 2:
                fragment = new MeetingListFragment();
                break;
            case 3:
                fragment = new MapFragment();
                break;
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_container, fragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.navbar_title_create);
                break;
            case 2:
                mTitle = getString(R.string.navbar_title_meetings);
                break;
            case 3:
                mTitle = getString(R.string.navbar_title_settings);
                break;
        }
        mTitle = getString(R.string.navbar_title_profile);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        if (id == R.id.action_example) {
            FindMe.please(this, 60000, true, new FindMe.FindMeListener() {
                @Override
                public void foundLocation(String provider, Location location) {

                    if (count == 0) {
                        Log.i("Findme", "lat = " + location.getLatitude() + " long = " + location.getLongitude());
                        Toast.makeText(getApplicationContext(), "lat = " + location.getLatitude() + " long = " + location.getLongitude(), Toast.LENGTH_LONG).cancel();
                        Toast.makeText(getApplicationContext(), "lat = " + location.getLatitude() + " long = " + location.getLongitude(), Toast.LENGTH_LONG).show();

                    }
                    count++;
                }

                @Override
                public void couldntFindLocation() {
                    Toast.makeText(getApplicationContext(), "Please turn GPS on ", Toast.LENGTH_SHORT).cancel();
                    Toast.makeText(getApplicationContext(), "Please turn GPS on ", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
