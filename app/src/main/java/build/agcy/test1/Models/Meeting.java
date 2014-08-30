package build.agcy.test1.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public class Meeting implements Parcelable {
    public String Id;
    public String description;
    public String creator;
    public String longtitude;
    public String latitude;
    public int time;

    public Meeting(String Id, String description,String creator,String longtitude,String latitude,int time) {
        this.Id = Id;
        this.description = description;
        this.creator = creator;
        this.longtitude = longtitude;
        //todo:исправить на серверн на longitude
        //id не получаю
        this.latitude = latitude;
        this.time = time;
    }
    public void setMeetingId(String Id) {
        this.Id = Id;}
    public String getMeetingId() {
        return Id;}
    public void setDescription(String description) {
        this.description = description;}
    public String getDescription() {
        return description;}
    public void setCreator(String creator) {
        this.creator = creator;}
    public String getCreator() {
        return creator;}
    public void setLatitude(String latitude) {
        this.latitude = latitude;}
    public String getLatitude() {
        return latitude;}
    public void setLongitude(String longitude) {
        this.longtitude = longitude;}
    public String getLongitude() {
        return longtitude;}
    public void setTime(int time) {
        this.time = time;}
    public int getTime() {
        return time;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(description);
        dest.writeString(creator);
        dest.writeString(longtitude);
        dest.writeString(latitude);
        dest.writeInt(time);
    }

    public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>() {

        @Override
        public Meeting createFromParcel(Parcel source) {
            String Id = source.readString();
            String description = source.readString();
            String creator = source.readString();
            String latitude = source.readString();
            String longtitude = source.readString();
            int time = source.readInt();
            return new Meeting(Id, description,creator, latitude, longtitude, time );
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };
}