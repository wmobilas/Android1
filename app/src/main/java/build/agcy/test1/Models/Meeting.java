package build.agcy.test1.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public class Meeting implements Parcelable {
    public String meeting_id;
    public String description;
    public String creator;
    public String longitude;
    public String latitude;
    public int time;

    public Meeting(String meeting_id, String description,String creator,String longtitude,String latitude,int time) {
        this.meeting_id = meeting_id;
        this.description = description;
        this.creator = creator;
        this.longitude = longtitude;
        this.latitude = latitude;
        this.time = time;
    }
    public void setMeetingId(String meeting_id) {
        this.meeting_id = meeting_id;
    }
    public String getMeetingId() {
        return meeting_id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setCreator(String creator) {
        this.creator = creator;
    }
    public String getCreator() {
        return meeting_id;
    } public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLatitude() {
        return latitude;
    }public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getLongitude() {
        return longitude;
    }public void setTime(int time) {
        this.time = time;
    }
    public int getTime() {
        return time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(meeting_id);
        dest.writeString(description);
        dest.writeString(creator);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeInt(time);
    }

    public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>() {

        @Override
        public Meeting createFromParcel(Parcel source) {
            String meeting_id = source.readString();
            String description = source.readString();
            String creator = source.readString();
            String latitude = source.readString();
            String longitude = source.readString();
            int time = source.readInt();
            return new Meeting(meeting_id, description,creator, latitude, longitude, time );
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };
}