package build.agcy.test1.Meetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;


public class MeetingHistoryAdapter extends MeetingListAdapter {

    public MeetingHistoryAdapter(Context context, ArrayList<Meeting> meetings) {
        super(context, meetings);
    }

    //todo не показывать не мои встречи и отображать количество предложений от accepterов
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup view;
        if (convertView == null) {
            view = (ViewGroup) inflater.inflate(R.layout.item_meeting_history, parent, false);
        } else {
            view = (ViewGroup) convertView;
        }
        TextView descView = (TextView) view.findViewById(R.id.description);
        TextView ownerNameView = (TextView) view.findViewById(R.id.owner_name);
        TextView openUsernameView = (TextView) view.findViewById(R.id.open_username);
        ImageView ownerPhotoView = (ImageView) view.findViewById(R.id.owner_photo);
        final TextView confirmerNameTextView = (TextView) view.findViewById(R.id.confirmer_name);
        ImageView confirmerPhotoView = (ImageView) view.findViewById(R.id.confirmer_photo);
        ImageView open = (ImageView) view.findViewById(R.id.lock);
        final Meeting meeting = getItem(position);
        ownerNameView.setText(meeting.owner.username);
        String ownerPhotoUrl = meeting.owner.photo;
        confirmerPhotoView.setVisibility(View.VISIBLE);
        ownerNameView.setVisibility(View.VISIBLE);
        openUsernameView.setVisibility(View.GONE);
//        open.setVisibility(View.GONE);
        confirmerNameTextView.setVisibility(View.VISIBLE);
        ownerPhotoView.setVisibility(View.VISIBLE);
        if (meeting.confirmer != null) {
            open.setImageResource(R.drawable.closed);
            if (ownerPhotoUrl != null) {
                ImageLoader.getInstance().displayImage(ownerPhotoUrl, ownerPhotoView);
            }
            confirmerNameTextView.setText(meeting.confirmer.username);
            if (meeting.confirmer.photo != null) {
                ImageLoader.getInstance().displayImage(meeting.confirmer.photo, confirmerPhotoView);
            }
        } else {
            open.setImageResource(R.drawable.open);
            openUsernameView.setVisibility(View.VISIBLE);
            confirmerPhotoView.setVisibility(View.GONE);
            confirmerNameTextView.setVisibility(View.GONE);
            ownerPhotoView.setVisibility(View.GONE);
            ownerNameView.setVisibility(View.GONE);
        }
        //Converters.getStaticMapImageUrl(meeting.longitude, meeting.latitude, 600, 400, 7, "red", "Here");
        descView.setText(meeting.description);
        return view;
    }
}
