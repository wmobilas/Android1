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
import build.agcy.test1.Models.MeetingsAccept;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class MeetingAcceptsListTask extends ApiTaskBase<MeetingsAccept[]> {
    // todo: 3) получаем список предложений встретиться на созданную встречу
    // отправляем string id
    // получаем массив MeetingsAccept{ string id; string accepterId; }
    public MeetingAcceptsListTask(final String id) {
        super("api/meeting/acceptslist", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("id",id));
        }}, false, false);
    }
    @Override
    protected Object doInBackground(Object... params) {
        try {
            String url = apiUrl + methodName;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpRequestBase request;
            String args = URLEncodedUtils.format(nameValuePairs, "utf-8");
            url += "?" + args;
            request = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            String responseStr;
            responseStr = EntityUtils.toString(httpEntity);
            Log.i(LOG_TAG, "Server response " + responseStr);
            return (responseStr);
        } catch (Exception exp) {
            Log.e(LOG_TAG, "Loader error " + exp.toString());
            return exp;
        }
    }
    @Override
    public void onSuccess(MeetingsAccept[] meetings) {
    }
    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);
    }
    @Override
    public void onError(Exception exp) {
    }
    @Override
    protected MeetingsAccept[] parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson((json), MeetingsAccept[].class);
    }
}
