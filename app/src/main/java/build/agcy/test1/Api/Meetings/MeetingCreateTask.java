package build.agcy.test1.Api.Meetings;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.util.ArrayList;

import build.agcy.test1.Api.ApiTaskBase;
import build.agcy.test1.EatWithMeApp;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class MeetingCreateTask extends ApiTaskBase<String> {
//    public MeetingCreateTask(String methodName, ArrayList<NameValuePair> args){
//        super(methodName, args, true, false);
//    }
    public MeetingCreateTask(final String latitude, final String longitude, final String time, final String selectedType) {
        super("meeting/create", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("latitude",latitude));
            add(new BasicNameValuePair("longitude",longitude));
            add(new BasicNameValuePair("time",time));
            add(new BasicNameValuePair("description",selectedType));
        }}, true, true);
    }
    @Override
    protected String parse(String json) throws JSONException {
        //todo: parse
        return String.valueOf(new Gson().fromJson(json,MeetingCreateResponse.class).id);
    }
    private class MeetingCreateResponse{
        public int id;
    }
}
