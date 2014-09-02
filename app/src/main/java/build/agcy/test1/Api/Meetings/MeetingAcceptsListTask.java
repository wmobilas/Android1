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
public abstract class MeetingAcceptsListTask extends ApiTaskBase<Meeting.Accept[]> {
    public MeetingAcceptsListTask(final String id) {
        super("meeting/acceptslist", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("id",id));
        }}, false, true);
    }

    @Override
    protected Meeting.Accept[] parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson((json), Meeting.Accept[].class);
    }
}
