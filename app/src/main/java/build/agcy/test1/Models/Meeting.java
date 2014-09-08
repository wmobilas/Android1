package build.agcy.test1.Models;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public class Meeting {
    public String id;
    public String description;
    public String longitude;
    public String latitude;
    public int time;
    public Accept accept;
    public int acceptsCount;
    public User owner;
    public User confirmer;

    public boolean isConfirmed() {
        return confirmer != null;
    }


    /*
    public void setMeetingId(String id) {
        this.id = id;}
    public String getMeetingId() {
        return id;}
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
        this.longitude = longitude;}
    public String getLongitude() {
        return longitude;}
    public void setTime(int time) {
        this.time = time;}
    public int getTime() {
        return time;}


    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(creator);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeInt(time);
    }

    public static final Parcelable.Creator<Meeting> CREATOR = new Parcelable.Creator<Meeting>() {

        @Override
        public Meeting createFromParcel(Parcel source) {
            String id = source.readString();
            String description = source.readString();
            String creator = source.readString();
            String latitude = source.readString();
            String longitude = source.readString();
            int time = source.readInt();
            return new Meeting(id, description,creator, latitude, longitude, time );
        }

        @Override
        public Meeting[] newArray(int size) {
            return new Meeting[size];
        }
    };
    */
    public static class Accept {
        public String id;
        public String meetingId;
        public User acceptor;
        public String message;
        public int time;
    }
}