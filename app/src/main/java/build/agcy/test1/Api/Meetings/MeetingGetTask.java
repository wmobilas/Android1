package build.agcy.test1.Api.Meetings;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
        }}, false, false);
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
