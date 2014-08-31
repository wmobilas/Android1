package build.agcy.test1.Api.Meetings;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
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
public class MeetingAcceptTask extends ApiTaskBase<String> {
    // todo: 2) предлагаем встретиться
    // отправляем string id втречи
    public MeetingAcceptTask(final String id, final String message) {
        super("meeting/accept", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("id",id));
            add(new BasicNameValuePair("text",message));
        }}, true, true);
    }
    @Override
    protected String parse(String json) throws JSONException {

        return new Gson().fromJson(json,String.class);
    }
    @Override
    public void onSuccess(String response) {
    }
    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
    }
    @Override
    public void onError(Exception exp) {
    }
}
