package build.agcy.test1.Meetings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.SocketException;

import build.agcy.test1.Api.Errors.ApiError;
import build.agcy.test1.Api.Meetings.MeetingCreateTask;
import build.agcy.test1.Core.Helpers.TimeConverter;
import build.agcy.test1.Core.MyLocationListener;
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

        if (savedInstanceState != null) {
            // Restore last state
        } else {
            meetingCreateView = inflater.inflate(R.layout.fragment_create_meeting, container, false);
            final Button createMeetingButton = (Button) meetingCreateView.findViewById(R.id.create_meeting);
            final Button createTimeButton = (Button) meetingCreateView.findViewById(R.id.time_pick);

            final MyLocationListener locationListener = new MyLocationListener(getActivity());
            locationListener.updateLocation();

            createTimeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TimePickerFragment(createTimeButton).show(getFragmentManager(), "TAG");
                }
            });
            createMeetingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final ProgressDialog dialog = new ProgressDialog(getActivity());
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setTitle("Creating meeting...");
                    dialog.setMessage("Please wait");
                    dialog.show();
                    EditText textBox = (EditText) meetingCreateView.findViewById(R.id.text1);

                    String text = "Simple lunch";
                    if (!textBox.getText().toString().equals("")) {
                        text = textBox.getText().toString();
                    }
                    // todo: implement location selector
                    final String latitude = "" + locationListener.getLatitude();
                    final String longitude = "" + locationListener.getLongitude();
                    locationListener.stopUsingGPS();
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
                                Toast.makeText(getActivity(), ((ApiError) exp).getMessage(), Toast.LENGTH_SHORT).show();
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
            });
        }
        return meetingCreateView;
    }
}