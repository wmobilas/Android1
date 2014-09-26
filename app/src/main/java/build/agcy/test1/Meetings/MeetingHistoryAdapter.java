package build.agcy.test1.Meetings;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import build.agcy.test1.EatWithMeApp;
import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;


public class MeetingHistoryAdapter extends MeetingListAdapter {

    public MeetingHistoryAdapter(Context context, ArrayList<Meeting> meetings) {
        super(context, meetings);
    }

    //todo не показывать не мои встречи
    //todo сделать динамическую загрузку
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup view;
        if (convertView == null) {
            view = (ViewGroup) inflater.inflate(R.layout.item_meeting_history, parent, false);
        } else {
            view = (ViewGroup) convertView;
        }
        final Meeting meeting = getItem(position);
        ((TextView) view.findViewById(R.id.description)).setText(meeting.description);
        final ImageView ownerPhotoView = (ImageView) view.findViewById(R.id.owner_photo);
        final TextView ownerNameView = (TextView) view.findViewById(R.id.owner_name);
        final TextView openTextView = (TextView) view.findViewById(R.id.open_username);
        final ImageView openImageView = (ImageView) view.findViewById(R.id.lock);
        final TextView confirmerNameTextView = (TextView) view.findViewById(R.id.confirmer_name);
        final ImageView confirmerPhotoView = (ImageView) view.findViewById(R.id.confirmer_photo);
        ownerNameView.setVisibility(View.GONE);
        ownerPhotoView.setVisibility(View.GONE);
        confirmerNameTextView.setVisibility(View.GONE);
        confirmerPhotoView.setVisibility(View.GONE);
        openTextView.setVisibility(View.VISIBLE);
        if (meeting.isConfirmed()) {
            //когото приняли уже
            openTextView.setText("Finished");
            openImageView.setImageResource(R.drawable.closed);
            ownerNameView.setText(meeting.owner.username);
            ownerNameView.setVisibility(View.VISIBLE);
            if (meeting.owner.photo != null) {
                new Handler().post(new Runnable() {
                    public void run() {
                        ImageLoader.getInstance().displayImage(meeting.owner.photo, ownerPhotoView);
                    }
                });
            } else {
                ownerPhotoView.setImageResource(R.drawable.doge);
            }
            ownerPhotoView.setVisibility(View.VISIBLE);
            confirmerNameTextView.setVisibility(View.VISIBLE);
            confirmerNameTextView.setText(meeting.confirmer.username);
            if (meeting.confirmer.photo != null) {
                new Handler().post(new Runnable() {
                    public void run() {
                        ImageLoader.getInstance().displayImage(meeting.confirmer.photo, confirmerPhotoView);
                    }
                });
            } else {
                confirmerPhotoView.setImageResource(R.drawable.doge);
            }
            confirmerPhotoView.setVisibility(View.VISIBLE);
        } else {
            //встреча открыта для заявок
            openImageView.setImageResource(R.drawable.open);
            openTextView.setText("OPEN");
            if (meeting.acceptsCount > 0) {
                //ктото дал заявку
                if (meeting.owner.id.equals(EatWithMeApp.currentUser.id)) {
                    //моя встреча
                    openTextView.setText(meeting.acceptsCount + " accepted your meeting");
                } else {
                    //не моя
                    openTextView.setText(meeting.acceptsCount + " accepted " + meeting.owner.username + " meeting");
                }
            }
            if (!meeting.owner.id.equals(EatWithMeApp.currentUser.id)) {
                ownerNameView.setText(meeting.owner.username);
                ownerNameView.setVisibility(View.VISIBLE);
                if (meeting.owner.photo != null) {
                    new Handler().post(new Runnable() {
                        public void run() {
                            ImageLoader.getInstance().displayImage(meeting.owner.photo, ownerPhotoView);
                        }
                    });
                } else {
                    ownerPhotoView.setImageResource(R.drawable.doge);
                }
                ownerPhotoView.setVisibility(View.VISIBLE);
            }
        }


        //Converters.getStaticMapImageUrl(meeting.longitude, meeting.latitude, 600, 400, 7, "red", "Here");
        return view;
    }
}