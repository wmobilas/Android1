package build.agcy.test1.Meetings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import build.agcy.test1.Models.Meeting;
import build.agcy.test1.R;

/**
 * Created by kiolt_000 on 08/09/2014.
 */
public class MeetingHistoryAdapter extends MeetingListAdapter {
    public MeetingHistoryAdapter(Context context, ArrayList<Meeting> meetings) {
        super(context, meetings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_meeting_history, null);
        TextView descView = (TextView) itemView.findViewById(R.id.description);

        TextView ownerNameView = (TextView) itemView.findViewById(R.id.owner_name);
        ImageView ownerPhotoView = (ImageView) itemView.findViewById(R.id.owner_photo);

        TextView statusView = (TextView) itemView.findViewById(R.id.status);


        final TextView confirmerNameTextView = (TextView) itemView.findViewById(R.id.confirmer_name);
        ImageView confirmerPhotoView = (ImageView) itemView.findViewById(R.id.confirmer_photo);


        final Meeting meeting = getItem(position);


        ownerNameView.setText(meeting.owner.username);
        String ownerPhotoUrl = meeting.owner.photo;
        // ImageLoader.getInstance().displayImage(ownerPhotoUrl, ownerPhotoView);


        if (meeting.confirmer != null) {
            confirmerNameTextView.setText(meeting.confirmer.username);
            String confirmerPhotoUrl = meeting.confirmer.photo;
            // ImageLoader.getInstance().displayImage(ownerPhotoUrl, confirmerPhotoView);
        } else {
            confirmerNameTextView.setText("Pending");
            confirmerPhotoView.setVisibility(View.GONE);
        }
        //Converters.getStaticMapImageUrl(meeting.longitude, meeting.latitude, 600, 400, 7, "red", "Here");

        descView.setText(meeting.description);
        return itemView;

    }
}
