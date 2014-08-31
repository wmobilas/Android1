package build.agcy.test1.Meetings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.R;

/**
* Created by kiolt_000 on 31/08/2014.
*/
public class CreateMeetingFragment extends android.app.Fragment {

    public CreateMeetingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_create_meeting, container, false);
        final SharedPreferences prefs = getActivity().getSharedPreferences("auth_prefs", Activity.MODE_PRIVATE);
        final Button createMeetingButton = (Button) rootView.findViewById(R.id.create_meeting);
        final Button timePickButton = (Button) rootView.findViewById(R.id.time_picker);
        final Button cancelButton = (Button) rootView.findViewById(R.id.cancel_meeting);
        final Button viewCreatedMeetingButton = (Button) rootView.findViewById(R.id.created_meeting_button);
        final TextView waitingAnswer = (TextView) rootView.findViewById(R.id.waiting);
        final int gon = View.GONE;
        final int vis = View.VISIBLE;

        if (prefs.contains("user_meeting_id")) {
            createMeetingButton.setVisibility(gon);
            timePickButton.setVisibility(gon);
            waitingAnswer.setVisibility(vis);
            cancelButton.setVisibility(vis);
            viewCreatedMeetingButton.setVisibility(vis);

        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMeetingButton.setVisibility(vis);
                timePickButton.setVisibility(vis);
                waitingAnswer.setVisibility(gon);
                cancelButton.setVisibility(gon);
                viewCreatedMeetingButton.setVisibility(gon);
                getActivity().getSharedPreferences("auth_prefs", Activity.MODE_PRIVATE).edit().remove("user_meeting_id").commit();
                getActivity().getSharedPreferences("auth_prefs", Activity.MODE_PRIVATE).edit().remove("user_meeting_time").commit();
                //todo сделать удаление на сервере
            }
        });
        viewCreatedMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
                Bundle bundle = new Bundle();
                bundle.putString("meeting_id", prefs.getString("created_meeting_id", "id"));
                bundle.putString("meeting_creator", EatWithMeApp.currentUser.username);
                bundle.putString("meeting_description", prefs.getString("created_meeting_description", "description"));
                bundle.putString("meeting_latitude", prefs.getString("created_meeting_latitude", "latitude"));
                bundle.putString("meeting_longitude", prefs.getString("created_meeting_longitude", "longitude"));
                Intent intent = new Intent(getActivity(), MeetingActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        createMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!prefs.contains("user_meeting_id")) {
                    createMeetingButton.setVisibility(gon);
                    timePickButton.setVisibility(gon);
                    waitingAnswer.setVisibility(vis);
                    cancelButton.setVisibility(vis);
                    viewCreatedMeetingButton.setVisibility(vis);
                    Spinner mySpinner = (Spinner) rootView.findViewById(R.id.MeetingType);
                    final String latitude = prefs.getString("user_lat", "33.333333");
                    final String longitude = prefs.getString("user_lng", "33.333333");
                    String selectedType = "Date";
                    try {
                        selectedType = mySpinner.getSelectedItem().toString();
                    } catch (Exception e) {
                        Toast.makeText(getActivity().getApplicationContext(), "no spinner item selected", Toast.LENGTH_SHORT).show();
                    }
                    final String finalSelectedType = selectedType;
                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setTitle("Creating meeting...");
                    dialog.setMessage("Please wait");
                    dialog.show();
                    String time;
                    if (!prefs.contains("user_meeting_time")) {
                        Calendar c = Calendar.getInstance();
                        time = String.valueOf((c.get(Calendar.HOUR_OF_DAY))) + "" + String.valueOf(c.get(Calendar.MINUTE));
                    } else {
                        time = prefs.getString("user_meeting_time", "00").substring(0, 2) + "" + prefs.getString("user_meeting_time", "00").substring(2);
                    }
                    if (time.equals("00")) {
                        time = "0000";
                    }
                    if (time.length() < 4) {
                        time = "0" + time;
                    }
                    MeetingCreateTask task = new MeetingCreateTask(latitude, longitude, time, finalSelectedType) {
                        @Override
                        public void onSuccess(final String i) {
                            dialog.dismiss();
                            SharedPreferences prefs = getActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
                            prefs.edit().putString("user_meeting_id", i).commit();
                            prefs.edit().putString("meeting_selected_creator", EatWithMeApp.currentUser.username).commit();
                            Bundle bundle = new Bundle();
                            bundle.putString("meeting_id", i);
                            bundle.putString("meeting_creator", EatWithMeApp.currentUser.username);
                            bundle.putString("meeting_description", finalSelectedType);
                            bundle.putString("meeting_latitude", latitude);
                            bundle.putString("meeting_longitude", longitude);
                            Intent intent = new Intent(getActivity(), MeetingActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
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
                }
            }
        });

        return rootView;
    }
}
