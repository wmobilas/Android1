package build.agcy.test1.Users;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.Arrays;

import build.agcy.test1.Api.Users.UsersListTask;
import build.agcy.test1.Chat.AndyChatActivity;
import build.agcy.test1.Meetings.MapActivity;
import build.agcy.test1.Models.User;
import build.agcy.test1.R;

public class UserListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
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

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_user_list, container, false);
            final ListView listView = (ListView) rootView.findViewById(R.id.list);
            UsersListTask task = new UsersListTask(new ArrayList<NameValuePair>()) {
                @Override
                public void onSuccess(User[] response) {

                    listView.setAdapter(new UserAdapter(getActivity(), new ArrayList<User>(Arrays.asList(response))));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putString("userid", "c68fe120-f4f4-41ff-b885-9cb97884704f");// вот такие айдишники будут у юзеров
                            Intent intent= new Intent(getActivity(), UserActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                }

                @Override
                public void onError(Exception exp) {
                    // Toast

                }
            };
            task.start();
            return rootView;
        }
    }
}
//    }
//    Bundle bundle = this.getIntent().getExtras();
//    //        Object message = getIntent().getStringExtra("EXTRA_MESSAGE");
////        String[] mess = new String[2];
//    String[] mess =(bundle.getStringArray("1"));
//    if ((mess[1]!="") || (mess[0]!="")){
//        CToast(mess[1], mess[0], "red");}
//    else{CToast("no", "thing", "red");}
//
//    public void chatOpen(View view) {
//        Intent intent = new Intent(this,AndyChatActivity.class);
//        startActivity(intent);
//    }
//
//
//    public void CToast(String t1, String t2, String c) {
//        if (c == "same") {
//            c = "444444";
//        } else if (c == "blue") {
//            c = "0099cc";
//        } else if (c == "red") {
//            c = "cc0000";
//        }
//
//        LayoutInflater inflater = getLayoutInflater();
//        View layout = inflater.inflate(R.layout.toast_layout,
//                (ViewGroup) findViewById(R.id.toast_layout_root));
//        TextView textCToast = (TextView) layout.findViewById(R.id.text);
//
//        String text2 = "<font color=#ffffff>" + t1 + "</font> <font color=#" + c + ">" + t2 + "</font";
//        textCToast.setText(Html.fromHtml(text2));
//
//        Toast toast = new Toast(this);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(layout);
//        toast.show();
