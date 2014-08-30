package build.agcy.test1.Users;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import build.agcy.test1.Models.User;
import build.agcy.test1.R;

public class UserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderUserFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
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
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderUserFragment extends Fragment {

        private String username;
        public User user;

        public PlaceholderUserFragment() {
        }
        public static View user_activity_View;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (user_activity_View != null) {
                ViewGroup parent = (ViewGroup) user_activity_View.getParent();
                if (parent != null)
                    parent.removeView(user_activity_View);
            }
            try{
                user_activity_View=inflater.inflate(R.layout.fragment_user, container, false);
            } catch (InflateException e) {
            }
            Activity activity = getActivity();
            if (activity != null) {
                Intent intent = activity.getIntent();
                Bundle bundle = intent.getExtras();
                username = bundle.getString("user_username");
                ((TextView) user_activity_View.findViewById(R.id.description)).setText(username);
                ((TextView) user_activity_View.findViewById(R.id.status)).setVisibility(View.GONE);
//                UserTask task = new UserTask(user_id) {
//                    @Override
//                    public void onSuccess(User user1) {
//                        user = user1;
//                        ((TextView) user_activity_View.findViewById(R.id.description)).setText(user.username);
//                        ((TextView) user_activity_View.findViewById(R.id.status)).setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError(Exception exp) {
//                        Toast.makeText(getActivity().getApplicationContext(),
//                                "usertask error" + exp.toString(), Toast.LENGTH_LONG).show();
//                        }
//                };
//                task.start();
            } else {
                user = new User() {{
                    id = "123";
                    username = "Ivan";
                }};
                ((TextView) user_activity_View.findViewById(R.id.description)).setText(user.username);
                ((TextView) user_activity_View.findViewById(R.id.status)).setVisibility(View.GONE);
            }
            return user_activity_View;
        }
    }
}


