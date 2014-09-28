package build.agcy.test1.Main;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.netcook.android.tools.CrashCatcherActivity;

import build.agcy.test1.Core.Helpers.FindMe;
import build.agcy.test1.Core.Helpers.OnBackPressedListener;
import build.agcy.test1.Fragments.LocationDialogFragment;
import build.agcy.test1.Fragments.MapFragment;
import build.agcy.test1.Meetings.CreateMeetingFragment;
import build.agcy.test1.R;

public class MainActivity extends CrashCatcherActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    @Override
    protected String getRecipient() {
        return "wmobilas@gmail.oom";
    }

    @Override
    protected Class<?> getStartActivityAfterCrached() {
        return MainActivity.class;
    }

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static long back_pressed;
    private static Location location;
    private CharSequence mTitle;
    private static boolean gotLocation = false;
    private static int pressedCount = 0;
    Fragment fragment = null;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    public static void applyTypeface(ViewGroup v, Typeface f) {
        if (v != null) {
            int vgCount = v.getChildCount();
            for (int i = 0; vgCount < 1; i++) {
                if (v.getChildAt(i) == null) continue;
                if (v.getChildAt(i) instanceof ViewGroup) {
                    applyTypeface((ViewGroup) v.getChildAt(i), f);
                } else {
                    View view = v.getChildAt(i);
                    if (view instanceof TextView) {
                        ((TextView) (view)).setTypeface(f);
                    } else if (view instanceof EditText) {
                        ((EditText) (view)).setTypeface(f);
                    } else if (view instanceof Button) {
                        ((Button) (view)).setTypeface(f);
                    }
                }
            }
        }
    }

    public static ViewGroup getParentView(View v) {
        ViewGroup vg = null;
        if (v != null) {
            vg = (ViewGroup) v.getRootView();
        }
        return vg;
    }

    public static Typeface getTypeface(Context c) {
        Typeface typeface = Typeface.createFromAsset(c.getAssets(), "fonts/fox.ttf");
        return typeface;
    }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.violet_dark);


    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                fragment = new ProfileFragment();
                break;
            case 1:
                fragment = new CreateMeetingFragment();
                break;
            case 2:
                fragment = new MapFragment();
                break;
//            case 3:
//                fragment = null;
//                break;
        }
        new Handler().post(new Runnable() {
            public void run() {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_container, fragment);
                ft.commit();
            }
        });
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.navbar_title_create);
                break;
            case 2:
                mTitle = getString(R.string.navbar_title_meetings);
                break;
//            case 3:
//                mTitle = getString(R.string.navbar_title_settings);
//                break;
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

            // Inflate the menu items for use in the action bar
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main, menu);
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
            FindMe.please(this, new FindMe.FindMeListener() {

                @Override
                public void foundLocation(String provider, Location location) {
                    MainActivity.location = location;
                    gotLocation = true;
                    if (pressedCount % 2 != 0) {
                        pressedCount--;
                    }
                }

                @Override
                public void couldntFindLocation() {
                    if (pressedCount % 2 != 0) {
                        pressedCount++;
                    }
                }
            });
            if (pressedCount % 2 == 0) {
                if (gotLocation) {
                    pressedCount++;
                    Log.i("Findme", "lat = " + location.getLatitude() + " long = " + location.getLongitude());
                    Toast.makeText(getApplicationContext(), "lat = " + location.getLatitude() + " long = " + location.getLongitude(), Toast.LENGTH_LONG).show();
                } else {
                    pressedCount--;
                    LocationManager locationMgr = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                    if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Toast.makeText(getApplicationContext(), "Please wait or choose other geo provider...", Toast.LENGTH_LONG).show();
                    } else {
                        LocationDialogFragment dialog = new LocationDialogFragment();
                        dialog.show(getFragmentManager(),
                                LocationDialogFragment.class.getName());
                    }
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (onBackPressedListener != null) {
            dosomething();
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
            else
                Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    public void dosomething() {
        super.onBackPressed();
    }

    protected OnBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }
}
