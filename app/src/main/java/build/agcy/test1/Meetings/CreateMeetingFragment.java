package build.agcy.test1.Meetings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.SocketException;

import build.agcy.test1.Api.Errors.ApiError;
import build.agcy.test1.Api.Meetings.MeetingCreateTask;
import build.agcy.test1.Core.Helpers.FindMe;
import build.agcy.test1.Core.Helpers.ListeningArrayAdapter;
import build.agcy.test1.Core.Helpers.SpinnerListener;
import build.agcy.test1.Core.Helpers.TimeConverter;
import build.agcy.test1.Fragments.LocationDialogFragment;
import build.agcy.test1.Fragments.TimePickerFragment;
import build.agcy.test1.R;

/**
 * Created by kiolt_000 on 31/08/2014.
 */
public class CreateMeetingFragment extends android.app.Fragment {

    public CreateMeetingFragment() {
    }

    //todo выбор из предустановленного списка если другое введи сам
    //todo выбирать язык встречи чтобы люди могли найти общий язык
    private static View meetingCreateView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            meetingCreateView = inflater.inflate(R.layout.fragment_create_meeting, container, false);
            final Button createMeetingButton = (Button) meetingCreateView.findViewById(R.id.create_meeting);
            final Button createTimeButton = (Button) meetingCreateView.findViewById(R.id.time_pick);
            final EditText textBox = (EditText) meetingCreateView.findViewById(R.id.text1);
            final Spinner mySpinner = (Spinner) meetingCreateView.findViewById(R.id.meeting_type);
            textBox.setVisibility(View.GONE);
            mySpinner.setVisibility(View.VISIBLE);
            final String selectedType = "Breakfast";
            ListeningArrayAdapter<CharSequence> adapter;

            final CharSequence[] items = {"Breakfast", "Lunch", "Dinner", "Other"};
            adapter = new ListeningArrayAdapter<CharSequence>(getActivity(), R.layout.spinner, items);
            adapter.addSpinnerListener(
                    new TestSpinnerListener(mySpinner, selectedType, textBox) {
                        @Override
                        public void onSpinnerExpanded() {
                        }

                        @Override
                        public void onSpinnerCollapsed() {
                            try {
                                selectedType = mySpinner.getSelectedItem().toString();
                            } catch (Exception e) {
                            }
                            if (selectedType.equals("Other")) {
                                textBox.setVisibility(View.VISIBLE);
                                mySpinner.setVisibility(View.GONE);
                            }
                        }
                    });
            mySpinner.setAdapter(adapter);

            createTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TimePickerFragment(createTimeButton).show(getFragmentManager(), "TAG");
                }
            });
            createMeetingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FindMe.please(getActivity(), new FindMe.FindMeListener() {
                        int count = 0;

                        @Override
                        public void foundLocation(String provider, Location location) {
                            if (count == 0) {
                                Log.i("CreateLocation", "lat = " + location.getLatitude() + " long = " + location.getLongitude());
                                final ProgressDialog dialog = new ProgressDialog(getActivity());
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
                                        Intent intent = new Intent(getActivity(), MeetingActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onError(Exception exp) {
                                        dialog.dismiss();
                                        if (exp instanceof ApiError) {
                                            //todo:коды
                                            Toast.makeText(getActivity(), (exp).getMessage(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (exp instanceof SocketException) {
//                                    Toast.makeText(getActivity().getApplicationContext(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                                            } else {
//                                    Toast.makeText(getActivity().getApplicationContext(), "Unexpected error", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                };
                                task.start();
                            }
                            count++;
                        }


                        @Override
                        public void couldntFindLocation() {
                            if (count == 0) {
                                LocationDialogFragment dialog = new LocationDialogFragment();
                                dialog.show(getFragmentManager(),
                                        LocationDialogFragment.class.getName());
                                count--;
                            }
                        }
                    });
                }
            });
        }
        return meetingCreateView;
    }

    private class TestSpinnerListener implements SpinnerListener {
        Spinner mySpinner;
        String selectedType;
        EditText textBox;

        public TestSpinnerListener(final Spinner mySpinner, final String selectedType, final EditText textBox) {

            this.mySpinner = mySpinner;
            this.selectedType = selectedType;
            this.textBox = textBox;
        }

        @Override
        public void onSpinnerExpanded() {
        }

        @Override
        public void onSpinnerCollapsed() {
        }
    }
}