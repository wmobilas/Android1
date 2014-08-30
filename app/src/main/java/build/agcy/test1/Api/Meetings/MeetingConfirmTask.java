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
    // todo: 4) соглашаемся на предложение
    // отправялем string id предложения
    public class MeetingConfirmTask extends ApiTaskBase<String> {
        public MeetingConfirmTask(final String id) {
            super("meeting/confirm", new ArrayList<NameValuePair>(){{
                add(new BasicNameValuePair("id",id));
            }}, true, true);
        }
        @Override
        protected String parse(String json) throws JSONException {

            return new Gson().fromJson(json,String.class);
        }
        @Override
        protected Object doInBackground(Object... params) {
            try {
                String url = apiUrl + methodName;

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpRequestBase request;
                request = new HttpPost(url);
                ((HttpPost) request).setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ((HttpPost) request).setHeader("Host", "eatwithme.azurewebsites.net");
                ((HttpPost) request).setHeader("Connection", "keep-alive");
                ((HttpPost) request).setHeader("DNT", "1");
                ((HttpPost) request).setHeader("Cookie", "ARRAffinity=d76c358de6a53bfcd43a2ddcf7362db09c42dac5980594e84d6f79551475489b; __RequestVerificationToken=twMglX9-9LrcX6z4X8mETeYbsOwPpTyZR682GF85BsOCG4-E58xic8SsJUTh1Pp2PvwU1tqxl-_9oQ4mPLmTZfV28YIcvtpSqD8oIfRrfE41; .AspNet.ApplicationCookie=1wo0PnI41z0eVmP1p3l4DMLPJxcQoihtusSsGcLEiFbYJZo1I22_UxixdpiSDRrexnGdsnzINyJ3A45OJ5jDfWiqp6RaFCHwcKrOE2aHrpLICPlynGx5wwYz4G67x6KreBS7k6-WC2CQIW-nm-CXpi5MB2RngD3ctUA_4FUYv2rENgELo6Sw4Q6WOaVgVoOFd858pT0xi0LSXWvvLRy9u-BPSPHxgP15lkq1F3opbT7TD_IiJU6w479mhlQ82V4PJUdBNxaYCqugFOwba1LaAdeeaONX4YoCjuMLX7wd6uGqzvkPfAHW_N7M_llWGfDu84YMe-Y83-8ZTsazc4qrGu1Eegkfv9HW2uqlcelJcIr8Bi6xMGovUDSE_BeKQ491KpD3E5ev_z6hEROAFr2qEd5T6m7TWqaqIfI50G6t2onrhtHNvEex6Uq0m0Bf7OkH");
                ((HttpPost) request).setHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4");
                ((HttpPost) request).setHeader("Referer", "http://eatwithme.azurewebsites.net/meeting/create");
                ((HttpPost) request).setHeader("Accept-Encoding", "gzip,deflate,sdch");
                ((HttpPost) request).setHeader("Origin", "http://eatwithme.azurewebsites.net");
                ((HttpPost) request).setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.143 Safari/537.36");
                ((HttpPost) request).setHeader("Content-Type", "application/x-www-form-urlencoded");
                ((HttpPost) request).setHeader("Cache-Control", "max-age=0");
                ((HttpPost) request).setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                BasicCookieStore cookies = new BasicCookieStore();
                cookies.addCookie(new BasicClientCookie(".AspNet.ApplicationCookie", EatWithMeApp.token));
                httpClient.setCookieStore(cookies);
                HttpResponse httpResponse = httpClient.execute(request);
                HttpEntity httpEntity = httpResponse.getEntity();
                String responseStr;
                responseStr = EntityUtils.toString(httpEntity);
                Log.i(LOG_TAG, "Server response " + responseStr);
                return responseStr;
            } catch (Exception exp) {
                Log.e(LOG_TAG, "Loader error " + exp.toString());
                return exp;
            }
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
