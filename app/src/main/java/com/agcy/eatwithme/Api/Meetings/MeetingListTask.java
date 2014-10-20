package com.agcy.eatwithme.Api.Meetings;

import com.agcy.eatwithme.Api.ApiTaskBase;
import com.agcy.eatwithme.Models.Meeting;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by kiolt_000 on 17/08/2014.
 */
public abstract class MeetingListTask extends ApiTaskBase<Meeting[]> {

    // отправляем int offset, int count, float latitude, float longitude
    // или отправляем массив ids
    public MeetingListTask(List<NameValuePair> nameValuePairs) {
        super("meeting/list", nameValuePairs, false, false);
    }

    //    @Override
//    protected Object doInBackground(Object... params) {
//        try {
//            String url = apiUrl + methodName;
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpRequestBase request;
//            String args = URLEncodedUtils.format(nameValuePairs, "utf-8");
//            url += "?" + args;
//            request = new HttpGet(url);
//            HttpResponse httpResponse = httpClient.execute(request);
//            HttpEntity httpEntity = httpResponse.getEntity();
//            String responseStr;
//            responseStr = EntityUtils.toString(httpEntity);
//            Log.i(LOG_TAG, "Server response " + responseStr);
//            return (responseStr);
//        } catch (Exception exp) {
//            Log.e(LOG_TAG, "Loader error " + exp.toString());
//            return exp;
//        }
//    }
//    @Override
//    public void onSuccess(Meeting[] meetings) {
//    }
//    @Override
//    protected void onPostExecute(Object response) {
//        super.onPostExecute(response);
//    }
//    @Override
//    public void onError(Exception exp) {
//    }
    @Override
    protected Meeting[] parse(String json) throws JSONException, FileNotFoundException {
        return new Gson().fromJson((json), Meeting[].class);
    }
}
