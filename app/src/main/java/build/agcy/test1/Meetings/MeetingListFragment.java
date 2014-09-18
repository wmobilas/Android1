package build.agcy.test1.Meetings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import build.agcy.test1.Api.Meetings.MeetingListTask;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

/**
 * Created by Freeman on 14.08.2014.
 */
public class MeetingListFragment extends Fragment {
    final String TAG = "agcy.test";
    private Activity myContext;

    public MeetingListFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "mapfragment onAttach");
        myContext = activity;
    }

    public static View myView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container,
                savedInstanceState);
        Log.d(TAG, "mapfragment onCreateView");
        if (savedInstanceState != null) {
            // Restore last state
            return myView;
        } else {
            myView = inflater.inflate(R.layout.fragment_meeting_list, container, false);
            final ListView meetingListView = (ListView) myView.findViewById(R.id.list);
            MeetingListTask task = new MeetingListTask(new ArrayList<NameValuePair>() {{
                add(new BasicNameValuePair("confirmed", "false"));
            }}) {
                @Override
                public void onSuccess(final Meeting[] response) {
                    myView.findViewById(R.id.history_status).setVisibility(View.GONE);
                    final MeetingListAdapter adapter = new MeetingListAdapter(getActivity(), new ArrayList<Meeting>(Arrays.asList(response)));
                    meetingListView.setAdapter(adapter);
                    meetingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putString("id", String.valueOf(response[position].id));
                            Intent intent = new Intent(getActivity(), MeetingActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onError(Exception exp) {
                    Toast.makeText(getActivity().getApplicationContext(), "MeetingListTaskError " + exp.toString(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "MeetingListTaskError " + exp.toString());
                }
            };
            task.start();
        }
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Fragment1 onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "Fragment1 onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Fragment1 onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "Fragment1 onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Fragment1 onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Fragment1 onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Fragment1 onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "Fragment1 onDetach");
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
//            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "mapfragment onCreate");

        SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set a custom tint color for all system bars
        tintManager.setTintColor(Color.parseColor("#800edc"));
        // set       a custom navigation bar resource
        tintManager.setNavigationBarTintResource(R.color.violet_dark);
    }
}