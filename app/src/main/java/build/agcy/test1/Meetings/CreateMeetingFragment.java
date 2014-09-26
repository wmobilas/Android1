package build.agcy.test1.Meetings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.SocketException;

import build.agcy.test1.Api.Errors.ApiError;
import build.agcy.test1.Api.Meetings.MeetingCreateTask;
import build.agcy.test1.Core.Helpers.BaseBackPressedListener;
import build.agcy.test1.Core.Helpers.FindMe;
import build.agcy.test1.Core.Helpers.ListeningArrayAdapter;
import build.agcy.test1.Core.Helpers.TimeConverter;
import build.agcy.test1.Fragments.LocationDialogFragment;
import build.agcy.test1.Fragments.TimePickerFragment;
import build.agcy.test1.Main.MainActivity;
import build.agcy.test1.R;

/**
 * Created by kiolt_000 on 31/08/2014.
 */
public class CreateMeetingFragment extends android.app.Fragment {

    public CreateMeetingFragment() {
    }

    //todo выбирать язык встречи чтобы люди могли найти общий язык
    private static View meetingCreateView;
    int pressedCount = 0;
    private static Activity activity;
    private static String selectedType = "Breakfast";

    private static Location location;
    private static boolean gotLocation = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        activity = getActivity();
        if (savedInstanceState == null) {
            meetingCreateView = inflater.inflate(R.layout.fragment_create_meeting, container, false);
            final Button createMeetingButton = (Button) meetingCreateView.findViewById(R.id.create_meeting);
            final Button createTimeButton = (Button) meetingCreateView.findViewById(R.id.time_pick);
            final EditText textBox = (EditText) meetingCreateView.findViewById(R.id.text1);
            final Button mySpinner = (Button) meetingCreateView.findViewById(R.id.spin);
//            textBox.setVisibility(View.GONE);
            mySpinner.setVisibility(View.VISIBLE);
            mySpinner.setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            } else {
                                ((MainActivity) activity).setOnBackPressedListener(new BaseBackPressedListener(activity) {
                                    // @Override
                                    // public void dosomething() {
                                    //todo починить чтоб возвращалось по клику назад на место
                                    //  mySpinner.setVisibility(View.VISIBLE);
                                    //  textBox.setVisibility(View.GONE);
//                                }
                                });
                            }
                        }
                    });
            final CharSequence[] items = {"Breakfast", "Lunch", "Dinner", "Other"};
            final LinearLayout bl = (LinearLayout) meetingCreateView.findViewById(R.id.bottom_line_divider);
            final ListeningArrayAdapter<CharSequence> adapter = new ListeningArrayAdapter<CharSequence>(activity, R.layout.spinner, items);
            mySpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(activity)
                            .setAdapter(adapter, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
//                                case SEL_ACTIVATE:
//                                    break;
                                        case 0:
                                            selectedType = "Breakfast";
                                            mySpinner.setText(selectedType);
                                            break;
                                        case 1:
                                            selectedType = "Lunch";
                                            mySpinner.setText(selectedType);
                                            break;
                                        case 2:
                                            selectedType = "Dinner";
                                            mySpinner.setText(selectedType);
                                            break;
                                        case 3:
                                            mySpinner.setVisibility(View.GONE);
                                            bl.setVisibility(View.GONE);
                                            break;
//                                case SEL_DEACTIVATE:
//                                    showListB(v);
//                                    break;
                                    }

                                    dialog.dismiss();
                                }
                            }).create().show();
                }
            });
            createTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TimePickerFragment(createTimeButton).show(getFragmentManager(), "TAG");
                }
            });
            createMeetingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FindMe.please(activity, new FindMe.FindMeListener() {

                        @Override
                        public void foundLocation(String provider, Location location) {
                            CreateMeetingFragment.location = location;
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
                            Log.i("CreateLocation", "lat = " + location.getLatitude() + " long = " + location.getLongitude());
                            final ProgressDialog dialog = new ProgressDialog(activity);
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setTitle("Creating meeting...");
                            dialog.setMessage("Please wait");
                            dialog.show();
                            String text = selectedType;
                            if (selectedType.equals("Other")) {
                                if (!textBox.getText().toString().equals("")) {
                                    text = textBox.getText().toString();
                                }
                            }
                            // todo: implement location selector
                            String latitude = "" + location.getLatitude();
                            String longitude = "" + location.getLongitude();
                            String time = createTimeButton.getText().toString();
                            if (time.equals(getResources().getString(R.string.pick_time))) {
                                time = String.valueOf(TimeConverter.getUnixNow());
                            } else {
                                time = time.substring(0, 2) + "" + time.substring(3);
                            }
                            final String finalText = text;
                            MeetingCreateTask task = new MeetingCreateTask(latitude, longitude, time, finalText) {
                                @Override
                                public void onSuccess(final String id) {
                                    dialog.dismiss();
                                    // todo: bad logic
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", id);
                                    Intent intent = new Intent(activity, MeetingActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(Exception exp) {
                                    dialog.dismiss();
                                    if (exp instanceof ApiError) {
                                        //todo:коды
                                        Toast.makeText(activity, (exp).getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (exp instanceof SocketException) {
//                                    Toast.makeText(activity.getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                                        } else {
//                                    Toast.makeText(activity.getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            };
                            task.start();
                        } else {
                            pressedCount--;
                            LocationManager locationMgr = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                            if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Toast.makeText(activity.getApplicationContext(), "Please wait or choose other geo provider...", Toast.LENGTH_LONG).show();
                            } else {
                                LocationDialogFragment dialog = new LocationDialogFragment();
                                dialog.show(getFragmentManager(),
                                        LocationDialogFragment.class.getName());
                            }
                        }
                    }
                }
            });
        }
        return meetingCreateView;
    }
}