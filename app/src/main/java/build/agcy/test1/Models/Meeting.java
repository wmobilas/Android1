package build.agcy.test1.Models;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public class Meeting {
    public String Id;
    public String description;
    public String creator;
    public String longitude;
    public String latitude;
    public int time;
    public String confirmer;

    public boolean isConfirmed() {
        return confirmer != null && !confirmer.equals("");
    }

    public Meeting(String Id, String description, String creator, String longitude, String latitude, int time) {
        this.Id = Id;
        this.description = description;
        this.creator = creator;
        this.longitude = longitude;
        //id не получаю
        this.latitude = latitude;
        this.time = time;
    }

    /*
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
        dest.writeString(Id);
        dest.writeString(description);
        dest.writeString(creator);
        dest.writeString(longitude);
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
            String longitude = source.readString();
            int time = source.readInt();
            return new Meeting(Id, description,creator, latitude, longitude, time );
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
        public String acceptorId;
        public String message;
        public int time;

        public Accept(String id, String acceptorId, String message) {
            this.id = id;
            this.acceptorId = acceptorId;
            this.message = message;
        }
    }
}