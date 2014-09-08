package build.agcy.test1.Api.Meetings;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import build.agcy.test1.Api.ApiTaskBase;
import build.agcy.test1.Models.Meeting;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class MeetingGetTask extends ApiTaskBase<Meeting> {
    public MeetingGetTask(final String id) {
        super("meeting/get", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("id",id));
        }}, false, true);
    }
    @Override
    protected Meeting parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson(json, Meeting.class);
    }

    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
    }

}
