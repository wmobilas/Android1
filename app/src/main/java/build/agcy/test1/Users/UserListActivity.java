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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.Arrays;

import build.agcy.test1.Api.Users.UsersListTask;
import build.agcy.test1.Models.User;
import build.agcy.test1.R;

public class UserListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.content_container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_list, menu);
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
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        public static View user_list_activity_View;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (user_list_activity_View != null) {
                ViewGroup parent = (ViewGroup) user_list_activity_View.getParent();
                if (parent != null)
                    parent.removeView(user_list_activity_View);
            }
            try {
                user_list_activity_View = inflater.inflate(R.layout.fragment_user_list, container, false);
            } catch (InflateException e) {
            }
            final ListView user_list_View = (ListView) user_list_activity_View.findViewById(R.id.user_list);
            UsersListTask task = new UsersListTask(new ArrayList<NameValuePair>()) {
                @Override
                public void onSuccess(final User[] response) {
                    user_list_activity_View.findViewById(R.id.user_list_status).setVisibility(View.GONE);
                    user_list_View.setAdapter(new UserAdapter(getActivity(), new ArrayList<User>(Arrays.asList(response))));
                    user_list_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id", response[position].id);
                            bundle.putString("user_photo", response[position].photo);
                            bundle.putString("user_username", response[position].username);
                            bundle.putString("user_latitude", response[position].latitude);
                            bundle.putString("user_latitude", response[position].longitude);
                            Intent intent = new Intent(getActivity(), UserActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                }

                @Override
                public void onError(Exception exp) {
                    Toast.makeText(getActivity().getApplicationContext(), "UserListTaskError " + exp.toString(), Toast.LENGTH_LONG).show();
                }
            };
            task.start();
            return user_list_activity_View;
        }
    }
}