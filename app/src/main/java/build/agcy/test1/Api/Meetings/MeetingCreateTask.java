package build.agcy.test1.Api.Meetings;

import android.util.Log;

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
public class MeetingCreateTask extends ApiTaskBase<String> {
//    public MeetingCreateTask(String methodName, ArrayList<NameValuePair> args){
//        super(methodName, args, true, false);
//    }
    public MeetingCreateTask(final String latitude, final String longitude, final String time, final String selectedType) {
        super("meeting/create", new ArrayList<NameValuePair>(){{
            add(new BasicNameValuePair("latitude",latitude));
            add(new BasicNameValuePair("longtitude",longitude));
            add(new BasicNameValuePair("time",time));
            add(new BasicNameValuePair("description",selectedType));
        }}, true, true);
    }
    public static String stripNonDigits(String input) {
        int j;
        int l = input.indexOf("!");
        if ((l < 5) && (l != -1)) {
            j = input.lastIndexOf("Get/") + 4;
            l = input.indexOf("<", j);
        } else {
            j = input.lastIndexOf(":") + 2;
            l = input.indexOf("\"", j);
        }
        String s3 = input.substring(j, l);
        return s3;
    }
    @Override
    protected String parse(String json) throws JSONException {
//        return new Gson().fromJson(json,.class);
        return stripNonDigits(json);
    }
@Override
protected Object doInBackground(Object... params) {
    try {
        String url = apiUrl + methodName;

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpRequestBase request;
        if (post) {
            request = new HttpPost(url);
            ((HttpPost) request).setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ((HttpPost) request).setHeader("Host","eatwithme.azurewebsites.net");
            ((HttpPost) request).setHeader("Connection","keep-alive");
            ((HttpPost) request).setHeader("DNT","1");
            ((HttpPost) request).setHeader("Cookie", "ARRAffinity=d76c358de6a53bfcd43a2ddcf7362db09c42dac5980594e84d6f79551475489b; __RequestVerificationToken=twMglX9-9LrcX6z4X8mETeYbsOwPpTyZR682GF85BsOCG4-E58xic8SsJUTh1Pp2PvwU1tqxl-_9oQ4mPLmTZfV28YIcvtpSqD8oIfRrfE41; .AspNet.ApplicationCookie=1wo0PnI41z0eVmP1p3l4DMLPJxcQoihtusSsGcLEiFbYJZo1I22_UxixdpiSDRrexnGdsnzINyJ3A45OJ5jDfWiqp6RaFCHwcKrOE2aHrpLICPlynGx5wwYz4G67x6KreBS7k6-WC2CQIW-nm-CXpi5MB2RngD3ctUA_4FUYv2rENgELo6Sw4Q6WOaVgVoOFd858pT0xi0LSXWvvLRy9u-BPSPHxgP15lkq1F3opbT7TD_IiJU6w479mhlQ82V4PJUdBNxaYCqugFOwba1LaAdeeaONX4YoCjuMLX7wd6uGqzvkPfAHW_N7M_llWGfDu84YMe-Y83-8ZTsazc4qrGu1Eegkfv9HW2uqlcelJcIr8Bi6xMGovUDSE_BeKQ491KpD3E5ev_z6hEROAFr2qEd5T6m7TWqaqIfI50G6t2onrhtHNvEex6Uq0m0Bf7OkH");
            ((HttpPost) request).setHeader("Accept-Language","ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
            ((HttpPost) request).setHeader("Referer","http://eatwithme.azurewebsites.net");
            ((HttpPost) request).setHeader("Accept-Encoding","gzip,deflate,sdch");
            ((HttpPost) request).setHeader("Origin","http://eatwithme.azurewebsites.net");
            ((HttpPost) request).setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36");
            ((HttpPost) request).setHeader("Content-Type","application/x-www-form-urlencoded");
            ((HttpPost) request).setHeader("Cache-Control","max-age=0");
            ((HttpPost) request).setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            if(loginRequred){
                BasicCookieStore cookies = new BasicCookieStore();
                cookies.addCookie(new BasicClientCookie(".AspNet.ApplicationCookie", EatWithMeApp.token));
            httpClient.setCookieStore(cookies);
            }
        } else {
            String args = URLEncodedUtils.format(nameValuePairs, "utf-8");
            url += "?" + args;
            request = new HttpGet(url);
        }
        HttpResponse httpResponse = httpClient.execute(request);
        HttpEntity httpEntity = httpResponse.getEntity();
        String responseStr;
        responseStr = EntityUtils.toString(httpEntity);
        Log.i(LOG_TAG, "Server response " + responseStr);
        return "{\"code\":\"1\",\"response\":{\"id\": \""+stripNonDigits(responseStr)+"\"}}";
    } catch (Exception exp) {
        Log.e(LOG_TAG, "Loader error " + exp.toString());
//        return exp;
        return exp;
    }
}
    @Override
    public void onSuccess(String response) {}
    @Override
    protected void onPostExecute(Object response) {
        super.onPostExecute(response);}
    @Override
    public void onError(Exception exp) {}
    // отправляем float latitute, float longitude, int time, string text
    // получае string id встречи
}
