package build.agcy.test1.Users;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import build.agcy.test1.Api.Users.UserTask;
import build.agcy.test1.Models.User;
import build.agcy.test1.R;

public class UserActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);// айдишники у нас везде стринг

        setContentView(R.layout.activity_user);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
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
    public static class PlaceholderFragment extends Fragment {

        private String userid;
        public User user;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_user, container, false);
            Activity activity = getActivity();
            if(activity!=null) {
                Intent intent = activity.getIntent();
                Bundle bundle = intent.getExtras();
                String id = bundle.getString("userid");
                this.userid = id;
            }else{
                userid = null;
            }
            UserTask task = new UserTask(userid) {


                @Override
                public void onSuccess(User user) {
                }

                @Override
                public void onError(Exception exp) {
                    ((TextView) rootView.findViewById(R.id.status)).setText("Error");
                    // обработка ошибки тоже нужна подробная, надеюсь, это не нужно объяснять?
                    // во всяком случае пока можно оставить так, мы ведь не продакшн делаем, где каждый левый еррор будет сказываться на
                    // количестве пользователей xD
                }
            };
            //task.start();
            user = new User(){{ id = "123"; username = "Ivan";}};
            PlaceholderFragment.this.user = user;
            ((TextView) rootView.findViewById(R.id.description)).setText(user.username);
            ((TextView) rootView.findViewById(R.id.status)).setVisibility(View.GONE);
            // теперь жди пока я прикручу апи) сделай пока каких-то фиктивных юзеров, чтобы раставить все по местами
            return rootView;
        }
    }
}
