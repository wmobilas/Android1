package com.agcy.eatwithme.Meetings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.agcy.eatwithme.Api.Meetings.MeetingGetTask;
import com.agcy.eatwithme.Api.Meetings.MeetingListTask;
import com.agcy.eatwithme.Models.Meeting;
import com.agcy.eatwithme.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Freeman on 14.08.2014.
 */
public class MeetingListFragment extends Fragment {
    final String TAG = "agcy.test";
    private Activity myContext;
    int i;

    public MeetingListFragment() {
    }

    MeetingListTask task;
    Meeting[] response;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel(true);
        }
        Log.d(TAG, "Fragment1 onDestroy");
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
        if (savedInstanceState == null) {
            myView = inflater.inflate(R.layout.fragment_meeting_list, null);
            SharedPreferences prefs = myContext.getPreferences(Activity.MODE_MULTI_PROCESS);
            if (!prefs.contains("list_meeting")) {
                prefs.edit().putString("list_meeting", "").commit();
            } else {
                prefs.edit().remove("list_meeting").commit();
                final ArrayList<Meeting> arrayList = new ArrayList<Meeting>();
                final ListView meetingList = (ListView) myView.findViewById(R.id.list);
                final MeetingListAdapter adptr = new MeetingListAdapter(myContext, arrayList);
                for (i = 0; i < 10; i++) {
                    new MeetingGetTask(String.valueOf(i)) {
                        @Override
                        public void onSuccess(Meeting response) {
                            arrayList.add(response);
                            adptr.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Exception exp) {

                        }
                    }.start();
                    meetingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            Bundle bundle = new Bundle();

                            bundle.putString("id", String.valueOf(adptr.getItem(position).id));
//                        bundle.putString("id", String.valueOf(response[(int) adptr.getItemId(position)].id));
//                            bundle.putString("id", String.valueOf(response[position].id));
                            Intent intent = new Intent(myContext, MeetingActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
                meetingList.setAdapter(adptr);
            }
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
    }
}