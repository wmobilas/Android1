package build.agcy.test1.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Freeman on 14.08.2014.
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {
    Button btn;
    int count = 0;

    public TimePickerFragment(Button createTimeButton) {
        this.btn = createTimeButton;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, final int hourOfDay, final int minute) {
        // Do something with the time chosen by the user
        boolean isMinuteDozen = (minute > 9);
        boolean isHourMoreZero = (hourOfDay > 9);
        boolean isNotShownYet = (count == 0);
        if (isNotShownYet) {
            String time = "0000";
            if (isMinuteDozen && isHourMoreZero) {
                time = "" + hourOfDay + " " + minute;
            } else {
                if (isMinuteDozen) {
                    time = "0" + hourOfDay + " " + minute;
                } else {
                    if (isHourMoreZero) {
                        time = "" + hourOfDay + " 0" + minute;
                    } else {
                        time = "0" + hourOfDay + " 0" + minute;
                    }
                }
            }
            btn.setText(time.substring(0, 2) + ":" + time.substring(3));
        }

        count++;
    }
}